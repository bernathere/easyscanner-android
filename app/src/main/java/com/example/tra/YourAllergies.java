package com.example.tra;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class YourAllergies extends Fragment {

    ListView itemList;
    ArrayList<String> allergenList;
    ArrayAdapter<String> adapter;

    AutoCompleteTextView ingredients;
    Button addButton;
    String[] allergens = new String[]{"Almond", "Barley", "Brazil Nut", "Cashew", "Celery", "Crustaceans", "Egg", "Fish", "Gluten", "Hazelnut", "Lactose", "Lupin", "Macadamia Nut", "Milk", "Mollusc", "Mustard", "Nut", "Oat", "Peanut", "Pecan", "Pistachio", "Rye", "Sesame", "Shellfish", "Soya", "Soybean", "Sulphite", "Sulphur dioxide", "Tree Nut", "Walnut", "Wheat"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_your_allergies_, container, false);

        ingredients = view.findViewById(R.id.TextViewIngredients);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, allergens);
        ingredients.setAdapter(arrayAdapter);

        if (getArrayList("dataList", getContext()) == null) {
            ArrayList<String> dataList = new ArrayList<>();
            saveArrayList(dataList, "dataList", getContext());
        }

        addButton = view.findViewById(R.id.buttonAdd);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newData = ingredients.getText().toString();
                if (!newData.isEmpty()) {
                    adapter.add(newData);
                    adapter.notifyDataSetChanged();
                    allergenList.add(newData);
                    saveArrayList(allergenList, "dataList", getContext());

                    ingredients.setText("");
                    getAllergies();
                }
            }
        });

        adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1);
        itemList = view.findViewById(R.id.itemListView);
        getAllergies();

        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                final int which_item = i;
                                                AlertDialog.Builder adb = new AlertDialog.Builder(view.getContext());
                                                adb.setTitle("Remove Item");
                                                adb.setMessage("Are you sure you want to remove this item from your allergy list?");
                                                adb.setNegativeButton("Cancel", null);
                                                adb.setPositiveButton("OK", new AlertDialog.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        //  String remove = allergenList.remove(which_item);
                                                        adapter.remove(allergenList.get(which_item));
                                                        adapter.notifyDataSetChanged();
                                                        allergenList.remove(which_item);
                                                        saveArrayList(allergenList, "dataList", getContext());
                                                    }
                                                });
                                                adb.show();
                                            }
                                        }
        );

        return view;
    }

    void getAllergies() {
        allergenList = getArrayList("dataList", getContext());
        adapter.clear();
        adapter.addAll(allergenList);
        adapter.notifyDataSetChanged();

        itemList.setAdapter(adapter);
    }

    public void saveArrayList(ArrayList<String> list, String key, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();
    }

    public static ArrayList<String> getArrayList(String key, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        return gson.fromJson(json, type);
    }
}