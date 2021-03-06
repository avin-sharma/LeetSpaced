package com.leetspaced.leetspaced.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leetspaced.leetspaced.MainViewModel;
import com.leetspaced.leetspaced.R;
import com.leetspaced.leetspaced.database.Question;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TodayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodayFragment extends Fragment implements QuestionsAdapter.ListItemClickListener, QuestionDetailsDialog.QuestionProvider{

    private final String TAG = TodayFragment.class.getSimpleName();
    private MainViewModel viewModel;
    private Question[] mQuestions;
    private Question lastClickedQuestion;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TodayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TodayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TodayFragment newInstance(String param1, String param2) {
        TodayFragment fragment = new TodayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_today, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Initialize ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        // All Questions RecyclerView
        setupRecyclerView(view);
    }

    private void setupRecyclerView(@NonNull View view) {
        final TextView emptyScreenMsgTextView = view.findViewById(R.id.today_empty_screen_text_view);
        final RecyclerView allQuestionsRecyclerView = view.findViewById(R.id.today_questions_recycler_view);
        final QuestionsAdapter adapter = new QuestionsAdapter(this);
        allQuestionsRecyclerView.setAdapter(adapter);

        viewModel.getTodaysQuestions().observe(getViewLifecycleOwner(), new Observer<Question[]>() {
            @Override
            public void onChanged(Question[] questions) {
                adapter.setmQuestions(questions);
                mQuestions = questions;

                if (mQuestions.length == 0) {
                    allQuestionsRecyclerView.setVisibility(View.GONE);
                    emptyScreenMsgTextView.setVisibility(View.VISIBLE);
                }
                else {
                    allQuestionsRecyclerView.setVisibility(View.VISIBLE);
                    emptyScreenMsgTextView.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onListItemClick(final int position) {
        lastClickedQuestion = mQuestions[position];
        QuestionDetailsDialog questionDetailsDialog = QuestionDetailsDialog.newInstance();
        questionDetailsDialog.show(getChildFragmentManager(), "QuestionDetailsDialog");
        Log.d(TAG, mQuestions[position].getTitle());
    }

    @Override
    public Question getClickedQuestion() {
        return lastClickedQuestion;
    }

    @Override
    public void updateClickedQuestion(Question question) {
        viewModel.updateQuestion(question);
    }
}