package com.reflect.reflect.ui.home;

import android.animation.Animator;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Consumer;
import androidx.emoji2.emojipicker.EmojiPickerView;
import androidx.emoji2.emojipicker.EmojiViewItem;
import androidx.lifecycle.ViewModelProvider;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.reflect.reflect.KeyboardUtils;
import com.reflect.reflect.R;
import com.reflect.reflect.RetrofitProvider;
import com.reflect.reflect.SharedPref;
import com.reflect.reflect.adapter.CustomAutoCompleteAdapter;
import com.reflect.reflect.databinding.FragmentHomeBinding;
import com.reflect.reflect.interfaces.ApiService;
import com.reflect.reflect.login.LoginActivity;
import com.reflect.reflect.model.Artist;
import com.reflect.reflect.model.Item;
import com.reflect.reflect.model.MyDay;
import com.reflect.reflect.model.SpotifyRecords;
import com.reflect.reflect.ui.BaseFragment;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends BaseFragment {

    private FragmentHomeBinding binding;
    private String DATE_PATTERN = "d' 'MMMM', 'yyyy";
    HomeViewModel homeViewModel;
    private  List<Item> items;
    Calendar calendar;
    Item selectedItem;
    private boolean shouldShowDropDown=true
            ;
    private static final long DELAY_MILLIS = 1000; // 2 seconds delay
    private Handler handler = new Handler();
    private Runnable apiRunnable;
    private ApiService apiService;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        calendar=Calendar.getInstance();
        binding.txtHappy.setOnClickListener(this::onClickFeeling);
        binding.txtAngry.setOnClickListener(this::onClickFeeling);
        binding.txtAwkward.setOnClickListener(this::onClickFeeling);
        binding.txtCrying.setOnClickListener(this::onClickFeeling);
        binding.txtSad.setOnClickListener(this::onClickFeeling);

        binding.btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sureToLogOut();
            }
        });
        binding.imgCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFeelingLikePicker();
            }
        });
        binding.songLyt.imgCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearSelectedSong();
            }
        });
        binding.txtAddMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEmojiPicker();
            }
        });
//        binding.txtDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showDatePicker();
//            }
//        });

        binding.searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                handler.removeCallbacks(apiRunnable);
                apiRunnable = new Runnable() {
                    @Override
                    public void run() {
                        // Call your API with the current text
                        searchSongs(editable.toString());
                    }
                };

                handler.postDelayed(apiRunnable, DELAY_MILLIS);

            }
        });

        binding.searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    KeyboardUtils.hideKeyboard( binding.searchBar);
                    return true; // Consume the event
                }
                return false; // Let the system handle other actions
            }
        });


        binding.searchBar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    selectedItem=items.get(i);
                    KeyboardUtils.hideKeyboard( binding.searchBar);
                    setSelectedItem();
                }
                catch (Exception e){
                    e.printStackTrace();}
            }
        });
        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveMyDay();
            }
        });

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        binding.txtDate.setText(new SimpleDateFormat(DATE_PATTERN, Locale.ENGLISH).format(calendar.getTimeInMillis()));
    }

    private void clearSelectedSong() {
        selectedItem=null;
        shouldShowDropDown=true;
        binding.lytSearchSong.setVisibility(View.VISIBLE);
        binding.songLyt.songLyt.setVisibility(View.GONE);
    }

    private void setSelectedItem() {
        shouldShowDropDown=false;
        binding.lytSearchSong.setVisibility(View.GONE);
        binding.songLyt.songLyt.setVisibility(View.VISIBLE);
        String url="";
        if(selectedItem.getAlbum()!=null && selectedItem.getAlbum().getImages()!=null && !selectedItem.getAlbum().getImages().isEmpty())
        {
            url=selectedItem.getAlbum().getImages().get(0).getUrl();
            Picasso.get().load(url).into(binding.songLyt.imageView);
        }
        binding.songLyt.txtSongName.setText(selectedItem.getName());
        StringBuilder artists = getArtists();
        binding.songLyt.txtSongArtist.setText(artists.toString());
    }

    @NonNull
    private StringBuilder getArtists() {
        StringBuilder artists= new StringBuilder();
        if (selectedItem.getArtists()!=null)
        {
            for (Artist artist : selectedItem.getArtists())
            {
                if(artists.length() == 0)
                {
                    artists = new StringBuilder(artist.getName());
                }
                else
                    artists.append(",").append(artist.getName());

            }
        }
        return artists;
    }

    private void searchSongs(String query) {
        apiService = RetrofitProvider.getApiService(new SharedPref(requireContext()).getString(SharedPref.KEY_TOKEN),RetrofitProvider.SPOTIFY_API_URL);
        apiService.searchTracks(query,"track",10).enqueue(new Callback<SpotifyRecords>() {
            @Override
            public void onResponse(Call<SpotifyRecords> call, Response<SpotifyRecords> response) {
                if(response.isSuccessful())
                {
                    SpotifyRecords body = response.body();
                    try {
                        items= body.getTracks().getItems();
                        binding.searchBar.setAdapter(new CustomAutoCompleteAdapter(requireContext(),items));

                        if(shouldShowDropDown)
                        {
                            binding.searchBar.showDropDown();
                        }

                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<SpotifyRecords> call, Throwable t) {
                Toast.makeText(requireContext(), "ERR", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sureToLogOut() {
        new MaterialAlertDialogBuilder(requireContext())
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("No",null)
                .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseAuth.getInstance().signOut();
                        requireActivity().finishAffinity();
                        requireActivity().finish();
                        startActivity(new Intent(requireContext(), LoginActivity.class));
                    }
                })
                .show();
    }

    private void showFeelingLikePicker() {
        binding.lytFeelingLike.setVisibility(View.GONE);
        binding.lytFeelingLikePicker.setVisibility(View.VISIBLE);
    }

    private void saveMyDay() {

        String uid = FirebaseAuth.getInstance().getUid();
        Long date =calendar.getTimeInMillis();
        String emoji = binding.txtFeelingLikeEmoji.getText().toString();
        String grateFulForToday = binding.etGrateFulfor.getText().toString();
        String journalToday = binding.etJournel.getText().toString();

        if (emoji.isEmpty()) {
            Toast.makeText(requireContext(), "Please Select Emoji", Toast.LENGTH_SHORT).show();
            return;
        }
//        if (grateFulForToday.isEmpty()) {
//            binding.etGrateFulfor.setError("Required Field");
//            binding.etGrateFulfor.requestFocus();
//            return;
//        }
//        if (journalToday.isEmpty()) {
//            binding.etJournel.setError("Required Field");
//            binding.etJournel.requestFocus();
//            return;
//        }
        showProgressDialog();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String id = db.collection("myday").document().getId();



        String name="";
        String imageUrl="";
        String artist="";
        if (selectedItem!=null)
        {

            name=selectedItem.getName();
            if(selectedItem.getAlbum()!=null && selectedItem.getAlbum().getImages()!=null && !selectedItem.getAlbum().getImages().isEmpty())
            {
                imageUrl=selectedItem.getAlbum().getImages().get(0).getUrl();
            }
            artist=getArtists().toString();
        }

        MyDay myDay = new MyDay(uid, id, date, emoji,name, imageUrl, artist, grateFulForToday, journalToday);

        db.collection("myday").document(date.toString()+myDay.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful() && task.getResult().exists() )
                {

                    hideProgress();
                    new  MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Entry exists")
                            .setMessage("Are you sure you want to rewrite todays entry?")
                            .setPositiveButton("No",null)
                            .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    showProgressDialog();
                                    insert(date, db, myDay);
                                }
                            })
                            .show();

                }
                else
                {
                    insert(date, db, myDay);
                }
            }
        });

    }

    private void insert(Long date, FirebaseFirestore db, MyDay myDay) {
        db.collection("myday").document(date.toString()+myDay.getUid()).set(myDay).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                hideProgress();
                if (task.isSuccessful()) {
                    showSuccessScreen();
                    clearFields();
                } else {
                    Toast.makeText(requireContext(), "Error failed to save.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showSuccessScreen() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.custom_progress_dialog);
        LottieAnimationView lottieAnimationView = dialog.findViewById(R.id.animationView);
        lottieAnimationView.setAnimation(R.raw.done);
        lottieAnimationView.loop(false);
        lottieAnimationView.playAnimation();
        lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                // Animation started
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                dialog.dismiss();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                // Animation canceled
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                // Animation repeated
            }
        });

        dialog.show();
    }

    private void clearFields() {
        clearSelectedSong();
        binding.searchBar.setText("");
        binding.etGrateFulfor.setText(null);
        binding.etJournel.setText(null);
        binding.txtFeelingLikeEmoji.setText(null);
        showFeelingLikePicker();
    }

    private void showDatePicker() {

        DatePickerDialog dialog = new DatePickerDialog(requireContext());
        dialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                binding.txtDate.setText(new SimpleDateFormat(DATE_PATTERN, Locale.ENGLISH).format(calendar.getTimeInMillis()));
            }
        });
        dialog.show();
    }

    void onClickFeeling(View view) {
        TextView textView = (TextView) view;
        showEmojiOnText(textView.getText());
    }

    private void showEmojiOnText(CharSequence text) {
        binding.txtFeelingLikeEmoji.setText(text);
        binding.lytFeelingLike.setVisibility(View.VISIBLE);
        binding.lytFeelingLikePicker.setVisibility(View.GONE);
    }

    private void showEmojiPicker() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.emoji_dialog);
        dialog.show();
        EmojiPickerView emojiPickerView = dialog.findViewById(R.id.emoji_picker);
        emojiPickerView.setOnEmojiPickedListener(new Consumer<EmojiViewItem>() {
            @Override
            public void accept(EmojiViewItem emojiViewItem) {
                showEmojiOnText(emojiViewItem.getEmoji());
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}