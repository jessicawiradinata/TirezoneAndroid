package id.co.tirezone.tirezonemobile;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AddProductActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("patterns");
        mRecyclerView = (RecyclerView) findViewById(R.id.product_recyclerview);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Pattern,EventViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Pattern, EventViewHolder>(
                Pattern.class,
                R.layout.item_addproduct,
                EventViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(final EventViewHolder viewHolder, Pattern model, int position) {
                viewHolder.setTitle(model.getName());
                viewHolder.setButton();
                final List<String> sizeList = model.getSizes();

                final String key = getRef(position).getKey();

                mDatabase.child(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String imageUrl = dataSnapshot.child("imageurl").getValue().toString();
                        viewHolder.setIcon(AddProductActivity.this, imageUrl);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                viewHolder.detailButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("stuff", key);
                        Intent i = new Intent(AddProductActivity.this, ProductDetailsActivity.class);
                        i.putExtras(bundle);
                        startActivity(i);
                    }
                });

                viewHolder.addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setupAddDialog(sizeList, key);
                    }
                });
            }
        };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.addproduct_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_mystore) {
            startActivity(new Intent(AddProductActivity.this, MyStoreActivity.class));
            return true;
        }
        else if (id == R.id.action_sales) {
            startActivity(new Intent(AddProductActivity.this, SalesActivity.class));
            return true;
        }
        else if (id == R.id.action_customers) {
            startActivity(new Intent(AddProductActivity.this, CustomersActivity.class));
            return true;
        }
        else if (id == R.id.action_profile) {
            startActivity(new Intent(AddProductActivity.this, ProfileActivity.class));
            return true;
        }
        else if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(AddProductActivity.this, LoginActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        View mView;
        Button detailButton;
        Button addButton;

        public EventViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setTitle(String title) {
            TextView productName = (TextView) mView.findViewById(R.id.product_name);
            productName.setText(title);
        }

        public void setIcon(Context context, String imageUrl) {
            ImageView tireIcon = (ImageView) mView.findViewById(R.id.tire_icon);
            Glide.with(context)
                    .load(imageUrl)
                    .into(tireIcon);
        }

        public void setButton() {
            detailButton = (Button) mView.findViewById(R.id.detail_button);
            addButton = (Button) mView.findViewById(R.id.add_button);
        }
    }

    private void setupAddDialog(List<String> list, final String key) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddProductActivity.this);
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

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(AddProductActivity.this,
                R.layout.support_simple_spinner_dropdown_item, list);
        final Spinner sizeSpinner = new Spinner(AddProductActivity.this);
        sizeSpinner.setAdapter(adapter);
        layout.addView(sizeSpinner);

        alertDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int price = Integer.parseInt(priceField.getText().toString());
                int stock = Integer.parseInt(stockField.getText().toString());
                String size = sizeSpinner.getSelectedItem().toString();

                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference()
                        .child("users").child(userId).child("products");
                DatabaseReference newSession = dataRef.push();
                newSession.child("pattern").setValue(key);
                newSession.child("price").setValue(price);
                newSession.child("size").setValue(size);
                newSession.child("stock").setValue(stock);

                Toast.makeText(AddProductActivity.this, "Product added to My Store", Toast.LENGTH_SHORT).show();
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
}


