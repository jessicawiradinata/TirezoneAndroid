package id.co.tirezone.tirezonemobile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.w3c.dom.Text;

public class MyStoreActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<Product, MyEventViewHolder> firebaseRecyclerAdapter;
    private String filter = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_store);

        mRecyclerView = (RecyclerView) findViewById(R.id.mystore_recyclerview);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("products");
    }

    @Override
    protected void onStart() {
        super.onStart();

        populateData();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        filter = "";
        firebaseRecyclerAdapter.cleanup();
    }

    @Override
    protected void onPause() {
        super.onPause();
        filter = "";
        firebaseRecyclerAdapter.cleanup();
    }

    @Override
    protected void onStop() {
        super.onStop();
        filter = "";
        firebaseRecyclerAdapter.cleanup();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mystore_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_mystore) {
            startActivity(new Intent(MyStoreActivity.this, MyStoreActivity.class));
            return true;
        }
        else if (id == R.id.action_sales) {
            startActivity(new Intent(MyStoreActivity.this, SalesActivity.class));
            return true;
        }
        else if (id == R.id.action_customers) {
            startActivity(new Intent(MyStoreActivity.this, CustomersActivity.class));
            return true;
        }
        else if (id == R.id.action_profile) {
            startActivity(new Intent(MyStoreActivity.this, ProfileActivity.class));
            return true;
        }
        else if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MyStoreActivity.this, LoginActivity.class));
            return true;
        }
        else if (id == R.id.action_add) {
            startActivity(new Intent(MyStoreActivity.this, AddProductActivity.class));
            return true;
        }
        else if (id == R.id.action_filter) {
            setupFilterDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class MyEventViewHolder extends RecyclerView.ViewHolder {
        View mView;
        Button detailButton;
        Button editButton;
        Button deleteButton;

        public MyEventViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setTitle(String title) {
            TextView productName = (TextView) mView.findViewById(R.id.product_name);
            productName.setText(title);
        }

        public void setSize(String theSize) {
            TextView size = (TextView) mView.findViewById(R.id.size);
            size.setText(theSize);
        }

        public void setPrice(int thePrice) {
            TextView price = (TextView) mView.findViewById(R.id.price);
            String priceString = Integer.toString(thePrice);
            price.setText("Rp " + priceString);
        }

        public void setStock(int theStock) {
            TextView stock = (TextView) mView.findViewById(R.id.stock);
            String stockString = Integer.toString(theStock);
            stock.setText(stockString + " stock(s) left");
        }

        public void setButton() {
            detailButton = (Button) mView.findViewById(R.id.detail_button);
            editButton = (Button) mView.findViewById(R.id.edit_button);
            deleteButton = (Button) mView.findViewById(R.id.delete_button);
        }
    }

    private void setupAlertDialog(String key) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MyStoreActivity.this);
        alertDialog.setCancelable(true);

        Context context = alertDialog.getContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50,50,50,0);

        final EditText priceField = new EditText(context);
        priceField.setHint("Price");
        priceField.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(priceField);

        final EditText stockField = new EditText(context);
        stockField.setHint("Stock");
        stockField.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(stockField);

        final String link = key;

        alertDialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String price = priceField.getText().toString();
                String stock = stockField.getText().toString();
                if(!price.equals("")) {
                    int priceInt = Integer.parseInt(price);
                    mDatabase.child(link).child("price").setValue(priceInt);
                }
                if(!stock.equals("")) {
                    int stockInt = Integer.parseInt(stock);
                    mDatabase.child(link).child("stock").setValue(stockInt);
                }
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.setView(layout);
        alertDialog.create();
        alertDialog.show();
    }

    private void setupDeleteDialog(String key) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MyStoreActivity.this);
        alertDialog.setCancelable(true);

        alertDialog.setTitle("Confirm Delete");
        alertDialog.setMessage("Delete this product from My Store?");
        final String link = key;
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDatabase.child(link).removeValue();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.create();
        alertDialog.show();
    }

    private void setupFilterDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MyStoreActivity.this);
        alertDialog.setCancelable(true);

        alertDialog.setTitle("Select tire size");
        alertDialog.setItems(R.array.size_list, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ListView listView = ((AlertDialog) dialog).getListView();
                String choice = listView.getItemAtPosition(which).toString();
                if(choice.equals("All sizes")) {
                    filter = "";
                }
                else {
                    filter = choice;
                }
                Log.v("FILTER ", filter);
                firebaseRecyclerAdapter.cleanup();
                populateData();
            }
        });

        alertDialog.create();
        alertDialog.show();
    }

    private void populateData() {
        Query query = mDatabase;
        if(!filter.equals("")) {
            query = mDatabase.orderByChild("size").equalTo(filter);
            Log.v("UPDATED QUERY ", query.toString());
        }
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Product, MyEventViewHolder>(
                Product.class,
                R.layout.item_mystore,
                MyEventViewHolder.class,
                query
        ) {
            @Override
            protected void populateViewHolder(MyEventViewHolder viewHolder, Product model, int position) {
                viewHolder.setTitle(model.getName());
                viewHolder.setSize(model.getSize());
                viewHolder.setPrice(model.getPrice());
                viewHolder.setStock(model.getStock());
                viewHolder.setButton();
                final String key = model.getPattern();
                final String link = getRef(position).getKey();
                viewHolder.detailButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("stuff", key);
                        Intent i = new Intent(MyStoreActivity.this, ProductDetailsActivity.class);
                        i.putExtras(bundle);
                        startActivity(i);
                    }
                });
                viewHolder.editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setupAlertDialog(link);
                    }
                });
                viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setupDeleteDialog(link);
                    }
                });
            }
        };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}
