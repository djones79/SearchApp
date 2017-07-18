package com.dljonesapps.search_o_matic;

import com.dljonesapps.search_o_matic.model.Doc;
import com.dljonesapps.search_o_matic.utils.NetworkChecker;
import com.dljonesapps.search_o_matic.view.EmailDialog;
import com.squareup.picasso.Picasso;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class DetailsActivity extends AppCompatActivity {

  private EmailDialog emailDialog;

  @BindView(R.id.article_headline)
  TextView headlineText;

  @BindView(R.id.article_text)
  TextView articleText;

  @BindView(R.id.article_image)
  ImageView imageView;

  @BindView(R.id.pub_date)
  TextView pubDate;

  @BindView(R.id.word_count)
  TextView wordCount;

  @BindView(R.id.fab)
  FloatingActionButton fab;

  private View.OnClickListener onClickListener;

  private Doc doc;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_details);
    ButterKnife.bind(this);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(view -> createEmailDialog());

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    Intent intent = getIntent();
    doc = (Doc) intent.getSerializableExtra("Article");

    headlineText.setText(doc.getHeadline().getPrintHeadline());
    articleText.setText(doc.getLeadParagraph());
    pubDate.setText(getResources().getString(R.string.pub_date, doc.getPubDate().toString()));
    wordCount.setText(getResources().getString(R.string.word_count, doc.getWordCount().toString()));

    if (doc.getMultimedia() != null && doc.getMultimedia().size() > 0) {
      String imageUrl = doc.getMultimedia().get(0).getUrl();
      Timber.d(doc.getMultimedia().toString());
      Picasso.with(this).load(imageUrl).into(imageView);
    }

    onClickListener = v -> openSettings();
  }

  private void createEmailDialog() {
    emailDialog = new EmailDialog(this);

    EditText address = (EditText) emailDialog.findViewById(R.id.email_address);
    EditText subject = (EditText) emailDialog.findViewById(R.id.email_subject);
    EditText message = (EditText) emailDialog.findViewById(R.id.message);
    TextView sendButton = (TextView) emailDialog.findViewById(R.id.button_email);

    sendButton.setOnClickListener(
        v -> {
          boolean error = false;

          if (TextUtils.isEmpty(address.getText())) {
            error = true;
            address.setError(getResources().getString(R.string.email_required));
          }

          if (TextUtils.isEmpty(subject.getText())) {
            error = true;
            subject.setError(getResources().getString(R.string.subject_required));
          }

          if (TextUtils.isEmpty(message.getText())) {
            error = true;
            message.setError(getResources().getString(R.string.message_required));
          }

          if (!error) {
            if (NetworkChecker.isNetworkAvailable(getApplicationContext())) {
              sendEmail(
                  address.getText().toString(),
                  subject.getText().toString(),
                  message.getText().toString());
            } else {
              Snackbar.make(
                      findViewById(R.id.activity_details),
                      getResources().getString(R.string.no_network_connection),
                      Snackbar.LENGTH_LONG)
                  .setAction(getResources().getString(R.string.open_settings), onClickListener)
                  .show();
            }
          }
        });
    emailDialog.show();
  }

  private void sendEmail(String email, String subject, String message) {
    String formattedString = String.format(getResources().getString(R.string.link), doc.getWebUrl(), message);
    Intent emailIntent = new Intent(Intent.ACTION_SEND);
    emailIntent.setType("message/rfc822");
    emailIntent.putExtra(Intent.EXTRA_EMAIL, email);
    emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
    emailIntent.putExtra(Intent.EXTRA_TEXT, formattedString);

    try {
      startActivity(Intent.createChooser(emailIntent, getResources().getString(R.string.send_message)));
      emailDialog.dismiss();
    } catch (android.content.ActivityNotFoundException e) {
      Snackbar.make(
              findViewById(R.id.activity_details),
              getResources().getString(R.string.no_email_client),
              Snackbar.LENGTH_LONG)
          .show();
    }
  }

  private void openSettings() {
    startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
  }
}
