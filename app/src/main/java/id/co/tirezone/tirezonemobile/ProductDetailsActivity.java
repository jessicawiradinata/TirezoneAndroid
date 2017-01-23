package id.co.tirezone.tirezonemobile;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

public class ProductDetailsActivity extends AppCompatActivity {
    private TextView titleField;
    private TextView rimField;
    private TextView aspectField;
    private TextView sectionField;
    private TextView speedField;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseReference mDatabase;

    private RecyclerView mRecyclerView2;
    private RecyclerView.LayoutManager mLayoutManager2;
    private DatabaseReference mDatabase2;

    private RecyclerView mRecyclerView3;
    private RecyclerView.LayoutManager mLayoutManager3;
    private DatabaseReference mDatabase3;

    private Firebase fRoot;
    private Firebase fRoot2;
    private String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        setReference();

        Bundle bundle = getIntent().getExtras();
        link = bundle.getString("stuff");
        fRoot = new Firebase("https://tirezonemobile.firebaseio.com/patterns/" + link);
        fRoot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> map = dataSnapshot.getValue(Map.class);
                getData(map);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        fRoot2 = new Firebase("https://tirezonemobile.firebaseio.com/patterns/" + link + "/specs");
        fRoot2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> map2 = dataSnapshot.getValue(Map.class);
                getSpecsData(map2);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference().child("patterns").child(link).child("features");
        mRecyclerView = (RecyclerView) findViewById(R.id.features_recyclerview);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("patterns").child(link).child("benefits");
        mRecyclerView2 = (RecyclerView) findViewById(R.id.benefits_recyclerview);
        mLayoutManager2 = new LinearLayoutManager(this);
        mRecyclerView2.setLayoutManager(mLayoutManager2);

        mDatabase3 = FirebaseDatabase.getInstance().getReference().child("patterns").child(link).child("sizes");
        mRecyclerView3 = (RecyclerView) findViewById(R.id.sizerange_recyclerview);
        mLayoutManager3 = new LinearLayoutManager(this);
        mRecyclerView3.setLayoutManager(mLayoutManager3);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<String,EventViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<String,EventViewHolder>(
                String.class,
                R.layout.item_bullet,
                EventViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(EventViewHolder viewHolder, String model, int position) {
                viewHolder.setContent(model);
            }
        };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);

        FirebaseRecyclerAdapter<String,BenefitsViewHolder> firebaseRecyclerAdapter2 = new FirebaseRecyclerAdapter<String,BenefitsViewHolder>(
                String.class,
                R.layout.item_bullet,
                BenefitsViewHolder.class,
                mDatabase2
        ) {
            @Override
            protected void populateViewHolder(BenefitsViewHolder viewHolder, String model, int position) {
                viewHolder.setContent(model);
            }
        };
        mRecyclerView2.setAdapter(firebaseRecyclerAdapter2);

        FirebaseRecyclerAdapter<String,SizesViewHolder> firebaseRecyclerAdapter3 = new FirebaseRecyclerAdapter<String,SizesViewHolder>(
                String.class,
                R.layout.item_bullet,
                SizesViewHolder.class,
                mDatabase3
        ) {
            @Override
            protected void populateViewHolder(SizesViewHolder viewHolder, String model, int position) {
                viewHolder.setContent(model);
            }
        };
        mRecyclerView3.setAdapter(firebaseRecyclerAdapter3);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_mystore) {
            startActivity(new Intent(ProductDetailsActivity.this, MyStoreActivity.class));
            return true;
        }
        else if (id == R.id.action_sales) {
            startActivity(new Intent(ProductDetailsActivity.this, SalesActivity.class));
            return true;
        }
        else if (id == R.id.action_customers) {
            startActivity(new Intent(ProductDetailsActivity.this, CustomersActivity.class));
            return true;
        }
        else if (id == R.id.action_profile) {
            startActivity(new Intent(ProductDetailsActivity.this, ProfileActivity.class));
            return true;
        }
        else if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(ProductDetailsActivity.this, LoginActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public EventViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setContent(String theContent) {
            TextView content = (TextView) mView.findViewById(R.id.content);
            content.setText(theContent);
        }
    }

    public static class BenefitsViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public BenefitsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setContent(String theContent) {
            TextView content = (TextView) mView.findViewById(R.id.content);
            content.setText(theContent);
        }
    }

    public static class SizesViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public SizesViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setContent(String theContent) {
            TextView content = (TextView) mView.findViewById(R.id.content);
            content.setText(theContent);
        }
    }

    private void setReference() {
        titleField = (TextView) findViewById(R.id.product_name);
        rimField = (TextView) findViewById(R.id.rim_size);
        aspectField = (TextView) findViewById(R.id.aspect_ratio);
        sectionField = (TextView) findViewById(R.id.section_width);
        speedField = (TextView) findViewById(R.id.speed_rating);
    }

    private void getData(Map<String, String> map) {
        String title = map.get("name");
        titleField.setText(title);
    }

    private void getSpecsData(Map<String, String> map) {
        String rimSize = map.get("rimsize");
        String aspectRatio = map.get("aspectratio");
        String sectionWidth = map.get("sectionwidth");
        String speedRating = map.get("speedrating");
        rimField.setText(rimSize);
        aspectField.setText(aspectRatio);
        sectionField.setText(sectionWidth);
        speedField.setText(speedRating);
    }

}
