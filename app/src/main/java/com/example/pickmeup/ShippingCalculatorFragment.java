package com.example.pickmeup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class ShippingCalculatorFragment extends Fragment implements View.OnClickListener {

    private AutoCompleteTextView countryTextView;
    private AutoCompleteTextView cityTextView;
    private AutoCompleteTextView zipCodeTextView;
    private AutoCompleteTextView lengthTextView;
    private AutoCompleteTextView widthTextView;
    private AutoCompleteTextView heightTextView;
    private AutoCompleteTextView weightTextView;
    private Button calculateButton;
    private Button resetButton;








    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View rootView = inflater.inflate(R.layout.fragment_shipping_calculator, container, false);

       countryTextView = rootView.findViewById(R.id.country_shipping_calculator);
       cityTextView = rootView.findViewById(R.id.city_shipping_calculator);
       zipCodeTextView = rootView.findViewById(R.id.zip_code_shipping_calculator);
       lengthTextView = rootView.findViewById(R.id.length_shipping_calculator);
       widthTextView = rootView.findViewById(R.id.width_shipping_calculator);
       heightTextView = rootView.findViewById(R.id.height_shipping_calculator);
       weightTextView = rootView.findViewById(R.id.weight_shipping_calculator);

       calculateButton = rootView.findViewById(R.id.submit_request_button);
       resetButton = rootView.findViewById(R.id.reset_button_shipping_calculator);

       calculateButton.setOnClickListener(this);
       resetButton.setOnClickListener(this);

       return rootView;
    }

    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = getFragmentManager();
        assert fragmentManager != null;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (v.getId()) {

            case R.id.submit_request_button:
                openShippingCalculatorDialog();
                break;
            case R.id.reset_button_shipping_calculator:
                ShippingCalculatorFragment shippingCalculatorFragment = new ShippingCalculatorFragment();
                fragmentTransaction.replace(R.id.fragment_container, shippingCalculatorFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
        }

    }

    private void openShippingCalculatorDialog() {

        ShippingCalculatorDialog shippingCalculatorDialog = new ShippingCalculatorDialog();
        assert getFragmentManager() != null;
        shippingCalculatorDialog.show(getFragmentManager(), "shipping calculator dialog");
    }
}
