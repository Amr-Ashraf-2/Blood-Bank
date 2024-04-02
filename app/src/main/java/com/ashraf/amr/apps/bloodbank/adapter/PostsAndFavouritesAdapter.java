package com.ashraf.amr.apps.bloodbank.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.ashraf.amr.apps.bloodbank.R;
import com.ashraf.amr.apps.bloodbank.data.model.client.ClientData;
import com.ashraf.amr.apps.bloodbank.data.model.post.postToggleFavourite.PostToggleFavourite;
import com.ashraf.amr.apps.bloodbank.data.model.post.posts.PostsData;
import com.ashraf.amr.apps.bloodbank.utils.HelperMethod;
import com.ashraf.amr.apps.bloodbank.view.activity.HomeCycleActivity;
import com.ashraf.amr.apps.bloodbank.view.fragment.homeCycle.post.PostDetailsFragment;
import com.ashraf.amr.mirtoast.ToastCreator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ashraf.amr.apps.bloodbank.data.api.RetrofitClient.getClient;
import static com.ashraf.amr.apps.bloodbank.data.local.SharedPreferencesManger.loadUserData;
import static com.ashraf.amr.apps.bloodbank.utils.HelperMethod.onLoadImageFromUrl;

public class PostsAndFavouritesAdapter extends RecyclerView.Adapter<PostsAndFavouritesAdapter.ViewHolder> {

    private Context context;
    private Activity activity;
    private List<PostsData> postsDataList = new ArrayList<>();
    private boolean favourites;
    private ClientData clientData;
    //private ApiService apiService;

    public PostsAndFavouritesAdapter(Context context,
                                     Activity activity,
                                     List<PostsData> postsDataList,
                                     boolean favourites) {
        this.context = context;
        this.activity = activity;
        this.postsDataList = postsDataList;
        this.favourites = favourites;
        clientData = loadUserData(activity);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_posts,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);
    }

    private void setData(ViewHolder holder, int position) {
        try {

            onLoadImageFromUrl(holder.postsItemIvPostPhoto, postsDataList.get(position).getThumbnailFullPath(), context);
            holder.postsItemTvTitle.setText(postsDataList.get(position).getTitle());
            if (postsDataList.get(position).getIsFavourite()) {
                holder.postsItemIvIsFavourite.setImageResource(R.drawable.ic_heart_solid);
            } else {
                holder.postsItemIvIsFavourite.setImageResource(R.drawable.ic_heart_regular);
            }

        } catch (Exception e) {
            //Toast.makeText( activity , e.getMessage() , Toast.LENGTH_SHORT).show();
        }

    }

    private void setAction(final ViewHolder holder, final int position) {
        holder.postsItemIvIsFavourite.setOnClickListener(v -> toggleFavourite(holder, position));


        holder.view.setOnClickListener(v -> {

            HomeCycleActivity navigationActivity = (HomeCycleActivity) activity;
            PostDetailsFragment postDetails = new PostDetailsFragment();
            postDetails.postsData = postsDataList.get(position);
            HelperMethod.replaceFragment(navigationActivity.getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, postDetails);

        });

    }
    private void toggleFavourite(final ViewHolder holder, final int position) {
        final PostsData postsData = postsDataList.get(position);

        postsDataList.get(position).setIsFavourite(!postsDataList.get(position).getIsFavourite());

        if (postsDataList.get(position).getIsFavourite()) {
            holder.postsItemIvIsFavourite.setImageResource(R.drawable.ic_heart_solid);
            ToastCreator.onCreateSuccessToast(activity,context.getResources().getString(R.string.add_to_favourite));


        } else {
            holder.postsItemIvIsFavourite.setImageResource(R.drawable.ic_heart_regular);

            ToastCreator.onCreateSuccessToast(activity,context.getResources().getString(R.string.remove_from_favourite));
            if (favourites) {
                postsDataList.remove(position);
                notifyDataSetChanged();
//                if (postsDataList.size() == 0) {
//                    articlesFragmentTvNoItems.setVisibility(View.VISIBLE);
//                }
            }
        }
        Call<PostToggleFavourite> call = getClient().getPostToggleFavourite(postsData.getId(), clientData.getApiToken());
        call.enqueue(new Callback<PostToggleFavourite>() {
            @Override
            public void onResponse(@NonNull Call<PostToggleFavourite> call, @NonNull Response<PostToggleFavourite> response) {
                try {
                    assert response.body() != null;
                    if (response.body().getStatus() == 1) {

                    } else {
                        postsDataList.get(position).setIsFavourite(!postsDataList.get(position).getIsFavourite());
                        if (postsDataList.get(position).getIsFavourite()) {
                            holder.postsItemIvIsFavourite.setImageResource(R.drawable.ic_heart_solid);
                            if (favourites) {
                                postsDataList.add(position, postsData);
                                notifyDataSetChanged();
                            }
                        } else {
                            holder.postsItemIvIsFavourite.setImageResource(R.drawable.ic_heart_regular);
                        }
                    }

                } catch (Exception e) {
                    //Toast.makeText( activity , e.getMessage() , Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NonNull Call<PostToggleFavourite> call, @NonNull Throwable t) {
                try {
                    postsDataList.get(position).setIsFavourite(!postsDataList.get(position).getIsFavourite());
                    if (postsDataList.get(position).getIsFavourite()) {
                        holder.postsItemIvIsFavourite.setImageResource(R.drawable.ic_heart_solid);
                        if (favourites) {
                            postsDataList.add(position, postsData);
                            notifyDataSetChanged();
                        }
                    } else {
                        holder.postsItemIvIsFavourite.setImageResource(R.drawable.ic_heart_regular);
                    }
                } catch (Exception e) {
                    //Toast.makeText( activity , e.getMessage() , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return postsDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.posts_item_iv_post_photo)
        PorterShapeImageView postsItemIvPostPhoto;
        @BindView(R.id.posts_item_tv_title)
        TextView postsItemTvTitle;
        @BindView(R.id.posts_item_iv_is_favourite)
        ImageView postsItemIvIsFavourite;
        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}
