    package com.app.tution.fragments;

    import android.content.Intent;
    import android.os.Bundle;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;

    import androidx.fragment.app.Fragment;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import com.app.tution.EnrollCalls;
    import com.app.tution.R;
    import com.app.tution.activities.ExpandCourseActivity;
    import com.app.tution.adapters.EnrolledCoursesAdapter;
    import com.app.tution.items.CourseClass;
    import com.app.tution.items.StudentClass;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.database.DatabaseReference;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.gson.Gson;

    public class HomeFragment extends Fragment {

        EnrollCalls calls;
        private StudentClass studentClass;
        private View mainView;
        private RecyclerView mainRecyclerView;

        private FirebaseAuth mAuth = FirebaseAuth.getInstance();
        private DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference();

        public HomeFragment() {
            // Required empty public constructor
        }

        public HomeFragment(StudentClass studentClass) {
            this.studentClass = studentClass;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            mainView = inflater.inflate(R.layout.fragment_home, container, false);

            mainRecyclerView = mainView.findViewById(R.id.mainRecyclerView);
            calls = new EnrollCalls() {
                @Override
                public void enroll(CourseClass course) {

                }

                @Override
                public void expandCourse(CourseClass course) {
                    Gson gson = new Gson();
                    String jCourse = gson.toJson(course);
                    Intent i = new Intent(getContext(), ExpandCourseActivity.class);
                    i.putExtra("jCourse", jCourse);
                    startActivity(i);
                }
            };

            EnrolledCoursesAdapter adapter = new EnrolledCoursesAdapter(getContext(), studentClass.getOptedCourses(), calls);
            mainRecyclerView.setAdapter(adapter);
            mainRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            return mainView;
        }

    }