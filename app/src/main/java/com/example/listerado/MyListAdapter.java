package com.example.listerado;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MyListAdapter extends ArrayAdapter<ListItemLists> {
    Context context;
    SharedpreferencesManager sharedpreferencesManager;

    public MyListAdapter(Context context, List<ListItemLists> items) {
        super(context, 0, items);
        this.context = context;
        sharedpreferencesManager = new SharedpreferencesManager(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate the view if it doesn't exist


        // Get the item at the current position
        ListItemLists item = getItem(position);


        //Prüft ob Liste dem eingeloggtem User gehört
        if (Objects.equals(item.getOwner_id(), sharedpreferencesManager.getId())) {


            convertView = LayoutInflater.from(getContext()).inflate(R.layout.template_owned_lists, parent, false);



            ImageView leave_delete_icon;
            leave_delete_icon = convertView.findViewById(R.id.leave_delete_icon);

            LinearLayout linearLayout;
            linearLayout = convertView.findViewById(R.id.my_lists_linearLayout);
            RelativeLayout settings, addUserToList, delete;
            settings = convertView.findViewById(R.id.configure_list);
            addUserToList = convertView.findViewById(R.id.add_user_to_list);
            delete = convertView.findViewById(R.id.delete_list);


            // Set the text of the TextView in the view
            TextView listName = convertView.findViewById(R.id.listName);
            TextView listIsFrom = convertView.findViewById(R.id.list_isFromUser);
            listName.setText(item.getText());
            listIsFrom.setText(item.getOwner_username());







            addUserToList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent addUserActivity = new Intent(context, AddUserToListActivity.class);
                    Bundle b = new Bundle();
                    b.putString("list_id", item.getId());
                    addUserActivity.putExtras(b);
                    context.startActivity(addUserActivity);
                    ((Activity) context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    ((Activity) context).finish();
                }
            });

            //Delete List
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setPositiveButton("Bestätigen", (dialogInterface, i) -> {
                        deleteList(item.getId(), position);
                    });

                    builder.setNegativeButton("Abbrechen", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });

            settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    View blub = LayoutInflater.from(context).inflate(R.layout.template_simple_dialog_popup_window, null);
                    builder.setView(blub);

                    EditText editText = blub.findViewById(R.id.simplePopup_editTextField);

                    builder.setPositiveButton("Erstellen", (dialogInterface, i) -> {
                        renameList(position, editText.getText().toString(), item.getId(), item.getOwner_username(), item.getOwner_id());
                    });

                    builder.setNegativeButton("Abbrechen", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent listActivity = new Intent(context, ListActivity.class);
                    Bundle b = new Bundle();
                    b.putString("id", item.getId()); //Your id
                    listActivity.putExtras(b);
                    context.startActivity(listActivity);
                    ((Activity) context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            });
        }   else {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.template_not_owned_lists, parent, false);



            // Set the text of the TextView in the view
            TextView listName = convertView.findViewById(R.id.listName);
            RelativeLayout leaveList;
            leaveList = convertView.findViewById(R.id.leave_list);
            LinearLayout linearLayout;
            linearLayout = convertView.findViewById(R.id.my_lists_linearLayout);
            TextView listIsFrom = convertView.findViewById(R.id.list_isFromUser);
            listName.setText(item.getText());
            listIsFrom.setText(item.getOwner_username());



            leaveList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setPositiveButton("Bestätigen", (dialogInterface, i) -> {
                        deleteList(item.getId(), position);
                    });

                    builder.setNegativeButton("Abbrechen", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent listActivity = new Intent(context, ListActivity.class);
                    Bundle b = new Bundle();
                    b.putString("id", item.getId()); //Your id
                    listActivity.putExtras(b);
                    context.startActivity(listActivity);
                    ((Activity) context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            });
        }




        return convertView;
    }


    

    void deleteList(String listId, int position) {
        String url = "http://bfi.bbs-me.org:2536/api/deleteList.php";
        final String[] jsonStatus = new String[1];
        final String[] jsonMessage = new String[1];
        final String[] jsonList_id = new String[1];
        final String[] jsonListUsername = new String[1];
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(response);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            if (jsonObject.has("status")) {
                                jsonStatus[0] = jsonObject.getString("status");
                            }
                            if (jsonObject.has("message")) {
                                jsonMessage[0] = jsonObject.getString("message");
                            }
                            if (jsonObject.has("list_id")) {
                                jsonList_id[0] = jsonObject.getString("list_id");
                            }
                            if (jsonObject.has("username")) {
                                jsonListUsername[0] = jsonObject.getString("username");
                            }
                        } catch (JSONException e) {
                            ToastManager.showToast(context, "Failed to parse server response!", Toast.LENGTH_SHORT);
                            e.printStackTrace();
                        }

                        if (jsonObject.has("status")) {
                            if (jsonStatus[0].equals("200")) {
                                ToastManager.showToast(context, jsonMessage[0], Toast.LENGTH_SHORT);
                                //System.out.println("\n\n\n\n\n\n" + jsonObject + "\n\n\n\n\n\n");
                                MyListsActivity.deleteFromList(position);
                                notifyDataSetChanged();
                            }
                        } else {
                            ToastManager.showToast(context, jsonMessage[0], Toast.LENGTH_SHORT);
                            //System.out.println("\n\n\n\n\n\n" + jsonMessage[0] + "\n\n\n\n\n\n");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastManager.showToast(context, "Verbindung zwischen Api und App unterbrochen (getUserLists)!", Toast.LENGTH_LONG);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("list_id", listId);
                params.put("user_id", sharedpreferencesManager.getId());
                params.put("hashed_password", sharedpreferencesManager.getHashed_password());
                return params;
            }
        };

        // Fügen Sie die Volley-Abfrage zur Warteschlange hinzu
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }


    public void renameList(int position, String newListName, String listID, String ownerName, String ownerID) {
        String url = "http://bfi.bbs-me.org:2536/api/renameList.php";
        final String[] jsonStatus = new String[1];
        final String[] jsonMessage = new String[1];
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(response);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            if (jsonObject.has("status")) {
                                jsonStatus[0] = jsonObject.getString("status");
                            }
                            if (jsonObject.has("message")) {
                                jsonMessage[0] = jsonObject.getString("message");
                            }

                        } catch (JSONException e) {
                            ToastManager.showToast(context, "Failed to parse server response!", Toast.LENGTH_SHORT);
                            e.printStackTrace();
                        }

                        if (jsonObject.has("status")) {
                            if (jsonStatus[0].equals("200")) {
                                ToastManager.showToast(context, jsonMessage[0], Toast.LENGTH_SHORT);
                                //System.out.println("\n\n\n\n\n\n" + jsonObject + "\n\n\n\n\n\n");


                                MyListsActivity.reloadLists(position, newListName, listID, ownerName, ownerID);

                                notifyDataSetChanged();
                            }   else  {
                                ToastManager.showToast(context, jsonMessage[0], Toast.LENGTH_SHORT);
                            }
                        } else {
                            ToastManager.showToast(context, jsonMessage[0], Toast.LENGTH_SHORT);
                            //System.out.println("\n\n\n\n\n\n" + jsonMessage[0] + "\n\n\n\n\n\n");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ToastManager.showToast(context, "Verbindung zwischen Api und App unterbrochen (renameList)!", Toast.LENGTH_LONG);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("list_id", listID);
                params.put("new_listname", newListName);
                params.put("user_id", sharedpreferencesManager.getId());
                params.put("hashed_password", sharedpreferencesManager.getHashed_password());
                return params;
            }
        };

        // Fügen Sie die Volley-Abfrage zur Warteschlange hinzu
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

}
