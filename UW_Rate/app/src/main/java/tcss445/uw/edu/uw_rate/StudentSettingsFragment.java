package tcss445.uw.edu.uw_rate;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import tcss445.uw.edu.uw_rate.API.API;
import tcss445.uw.edu.uw_rate.API.ReadStudentTask;
import tcss445.uw.edu.uw_rate.API.UpdateStudentTask;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link StudentSettingsFragment.StudentSettingsFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link StudentSettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudentSettingsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private StudentSettingsFragmentInteractionListener mListener;

    public StudentSettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StudentSettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StudentSettingsFragment newInstance(String param1, String param2) {
        StudentSettingsFragment fragment = new StudentSettingsFragment();
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
        View view = inflater.inflate(R.layout.fragment_student_settings, container, false);
        final EditText etStudentEmail = (EditText) view.findViewById(R.id.studentEmail);
        final EditText etStudentPassword = (EditText) view.findViewById(R.id.studentPassword);
        final EditText etStudentFirstName = (EditText) view.findViewById(R.id.studentFirstName);
        final EditText etStudentLastName = (EditText) view.findViewById(R.id.studentLastName);
        Button bStudentEmail = (Button) view.findViewById(R.id.submitSettings);

        final API.Listener readStudentTaskListener = new API.Listener() {
            @Override
            public void onComplete(Object results) {
                StudentResult[] students = (StudentResult[]) results;
                if (students != null && students.length > 0) {
                    StudentResult student = students[0];
                    etStudentEmail.setText(student.email);
                    etStudentFirstName.setText(student.first_name);
                    etStudentLastName.setText(student.last_name);
                }
            }

            @Override
            public void onError() {
                Log.d("error", "error");
            }
        };

        bStudentEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String studentEmail = etStudentEmail.getText().toString();
                final String studentPassword = etStudentPassword.getText().toString();
                final String studentFirstName = etStudentFirstName.getText().toString();
                final String studentLastName = etStudentLastName.getText().toString();
                new UpdateStudentTask(getContext(), new API.Listener() {
                    @Override
                    public void onComplete(Object results) {
                        new ReadStudentTask(getContext(), readStudentTaskListener).read();
                    }

                    @Override
                    public void onError() {

                    }
                }).update(studentEmail, studentPassword, studentFirstName, studentLastName);
            }
        });
        new ReadStudentTask(getContext(), readStudentTaskListener).read();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof StudentSettingsFragmentInteractionListener) {
            mListener = (StudentSettingsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface StudentSettingsFragmentInteractionListener {
    }
}
