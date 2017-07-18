package com.dljonesapps.search_o_matic;

import com.dljonesapps.search_o_matic.model.ApiResponse;
import com.dljonesapps.search_o_matic.rest.SearchApiService;
import com.dljonesapps.search_o_matic.rest.SearchClient;
import com.dljonesapps.search_o_matic.utils.NetworkChecker;
import com.dljonesapps.search_o_matic.utils.RecyclerItemClickListener;
import com.dljonesapps.search_o_matic.utils.SearchAdapter;
import com.dljonesapps.search_o_matic.view.SearchDialog;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

  private static final String ARTICLE = "Article";
  private static final String INITIAL_REQUEST = "Athletics";

  private SearchAdapter searchAdapter;
  private FloatingActionButton fab;

  @BindView(R.id.toolbar)
  Toolbar toolbar;

  private RecyclerView recyclerView;

  private View.OnClickListener onClickListener;

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(v -> createSearchDialog());

    Timber.plant(new Timber.DebugTree());

    setToolbarFont();

    if (recyclerView == null) {
      setupRecyclerView();
      loadStarterArticles();
    }

    onClickListener = v -> openSettings();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void createSearchDialog() {
    final SearchDialog searchDialog = new SearchDialog(this);
    SearchView searchView = (SearchView) searchDialog.findViewById(R.id.search_view);
    TextView searchButton = (TextView) searchDialog.findViewById(R.id.button_search);

    searchButton.setOnClickListener(
        v -> {
          if (!TextUtils.isEmpty(searchView.getQuery())) {
            searchArticles(searchView.getQuery().toString());
            searchDialog.dismiss();
          } else {
            Timber.e(getResources().getString(R.string.search_error));
          }
        });

    searchDialog.show();
  }

  private void setToolbarFont() {
    CollapsingToolbarLayout ctbLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

    Typeface face = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.font_path_krinkes_decor));
    ctbLayout.setExpandedTitleTypeface(face);
    ctbLayout.setCollapsedTitleTypeface(face);
  }

  private void setupRecyclerView() {
    recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, 1));
    searchAdapter = new SearchAdapter(this);
    recyclerView.setAdapter(searchAdapter);

    recyclerView.addOnItemTouchListener(
        new RecyclerItemClickListener(
            this,
            recyclerView,
            new RecyclerItemClickListener.OnItemClickListener() {
              @Override
              public void onItemClick(View view, int position) {
                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                intent.putExtra(ARTICLE, searchAdapter.getResponseList().get(position));
                startActivity(intent);
              }

              @Override
              public void onItemLongClick(View view, int position) {
                // no-op
              }
            }));
  }

  private void loadStarterArticles() {
    makeRequest(INITIAL_REQUEST);
  }

  private void searchArticles(String query) {
    makeRequest(query);
  }

  private void makeRequest(String query) {
    if (NetworkChecker.isNetworkAvailable(this)) {
      SearchApiService service = SearchClient.getClient().create(SearchApiService.class);

      Call<ApiResponse> call = service.getResponse(BuildConfig.SEARCH_API_KEY, query);

      call.enqueue(
          new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
              searchAdapter.setDocsList(response.body().getResponse().getDocs());
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
              Timber.e(t.getMessage());
              Snackbar.make(
                      findViewById(R.id.activity_main),
                      getResources().getString(R.string.failed_to_download),
                      Snackbar.LENGTH_LONG)
                  .show();
            }
          });
    } else {
      Snackbar.make(
              findViewById(R.id.activity_main), getResources().getString(R.string.no_network_connection),
              Snackbar.LENGTH_LONG)
          .setAction(getResources().getString(R.string.open_settings), onClickListener)
          .show();
    }
  }

  private void openSettings() {
    startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
  }
}
