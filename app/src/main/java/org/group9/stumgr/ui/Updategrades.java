package org.group9.stumgr.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.group9.stumgr.R;
import org.group9.stumgr.bean.Student;
import org.group9.stumgr.databinding.FragmentGradesBinding;
import org.group9.stumgr.databinding.FragmentUpdateProfileBinding;

public class Updategrades extends Fragment {
    private FragmentGradesBinding bd;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        bd = DataBindingUtil.inflate(
                inflater, R.layout.fragment_grades, container, false);

        return bd.getRoot();
    }

    public void setStudent(Student student) {
        bd.setStudent(student);
    }

}