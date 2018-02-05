package com.example.riyariya.riya1_subbook;

/**
 * Copyright (c) 2018. By Riya for CMPUT 301 as Assignment 1
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.google.gson.Gson;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Handling the individual list items and the view/edit and delete functionality.
 * used the tutorial from https://guides.codepath.com/android/Using-the-RecyclerView on Feb 4,2018
 * to handle basic recyclerview adapter structure. Implemented it for our requirements.
 * @author riya1
 * @version 1.0
 */
public class SubscriptionsAdapter extends RecyclerView.Adapter <SubscriptionsAdapter.ViewHolder>{

    private List<Subscription> mSubscriptions;
    private Context mContext;

    /**
     * Provide a direct reference to each of the views within a data item
     * Used to cache the views within the item layout for fast access
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView subscriptionTextView;
        private Button editButton;
        private Button deleteButton;

        /**
         * Constructor
         * @param itemView
         */
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            subscriptionTextView = (TextView) itemView.findViewById(R.id.subscriptionName);
            editButton = (Button) itemView.findViewById(R.id.editBtn);
            deleteButton = (Button) itemView.findViewById(R.id.deleteBtn);
        }
    }

    /**
     * Inflating a layout from XML and returning the holder
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public SubscriptionsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View subscriptionView = inflater.inflate(R.layout.list_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(subscriptionView);
        return viewHolder;
    }

    /**
     * To populate data into the item through holder
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(SubscriptionsAdapter.ViewHolder viewHolder, final int position) {
        // Get the data model based on position
        final Subscription subscription = mSubscriptions.get(position);

        final TextView textView = viewHolder.subscriptionTextView;
        textView.setText(subscription.toString());
        Button editButton = viewHolder.editButton;
        Button deleteButton = viewHolder.deleteButton;


        editButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Implements View Details and Edit functionality
             * @param v
             */
            @Override
            public void onClick(View v) {
                ((SubBookActivity) mContext).setViewElement(subscription);
           }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Implements Delete functionality
             * @param v
             */
            @Override
            public void onClick(View v) {
                mSubscriptions.remove(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();
                saveInFile();
            }
        });
    }

    /**
     * Returns the total count of items in the list
     * @return
     */
    @Override
    public int getItemCount() {
        return mSubscriptions.size();
    }

    /**
     * Constructs the adapter given an entire array of subscriptions
     * @param context
     * @param subscriptions
     */
    public SubscriptionsAdapter(Context context, ArrayList<Subscription> subscriptions) {
        mSubscriptions = subscriptions;
        mContext = context;
    }

    /**
     * For easy access to the context object in the recyclerview
     * @return
     */
    private Context getContext() {
        return mContext;
    }

    /**
     * Saves in file, to be used after deletion
     */
    private void saveInFile() {
        try {
            FileOutputStream fos = getContext().openFileOutput("file.sav", Context.MODE_PRIVATE);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            gson.toJson(mSubscriptions, out);
            out.flush();

        } catch (FileNotFoundException e) {
            throw new RuntimeException();

        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
