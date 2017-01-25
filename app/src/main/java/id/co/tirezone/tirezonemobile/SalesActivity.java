package id.co.tirezone.tirezonemobile;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SalesActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
    }

    @Override
    protected void onStart() {
        super.onStart();
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
            startActivity(new Intent(SalesActivity.this, MyStoreActivity.class));
            return true;
        }
        else if (id == R.id.action_sales) {
            startActivity(new Intent(SalesActivity.this, SalesActivity.class));
            return true;
        }
        else if (id == R.id.action_customers) {
            startActivity(new Intent(SalesActivity.this, CustomersActivity.class));
            return true;
        }
        else if (id == R.id.action_profile) {
            startActivity(new Intent(SalesActivity.this, ProfileActivity.class));
            return true;
        }
        else if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(SalesActivity.this, LoginActivity.class));
            return true;
        }
        else if (id == R.id.action_add) {
            startActivity(new Intent(SalesActivity.this, TransactionActivity1.class));
            return true;
        }
        else if (id == R.id.action_filter) {
            setupFilterDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class SalesViewHolder extends RecyclerView.ViewHolder {
        View mView;
        View detailButton;

        public SalesViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setInvoiceNo(String num) {
            TextView invoiceNo = (TextView) mView.findViewById(R.id.invoice_no);
            invoiceNo.setText(num);
        }

        public void setDate(String theDate) {
            TextView date = (TextView) mView.findViewById(R.id.date);
            date.setText(theDate);
        }

        public void setPrice(String thePrice) {
            TextView price = (TextView) mView.findViewById(R.id.price);
            price.setText(thePrice);
        }

        public void setVehicleId(String id) {
            TextView vehicleId = (TextView) mView.findViewById(R.id.vehicle_id);
            vehicleId.setText(id);
        }

        public void setButton() {
            detailButton = (View) mView.findViewById(R.id.item_sales);
        }
    }

    private void setupDetailButton() {
        LinearLayout detailButton = (LinearLayout) findViewById(R.id.item_sales);
        detailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SalesActivity.this, TransactionDetailsActivity.class));
            }
        });
    }

    private void setupFilterDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SalesActivity.this);
        alertDialog.setCancelable(true);

        Context context = alertDialog.getContext();
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50,50,50,0);

        final EditText dateFrom = new EditText(context);
        dateFrom.setHint("Date (from)");
        dateFrom.setInputType(InputType.TYPE_CLASS_DATETIME);
        dateFrom.setFocusable(false);
        layout.addView(dateFrom);
        setupDatePicker(dateFrom);

        final EditText dateTo = new EditText(context);
        dateTo.setHint("Date (to)");
        dateTo.setInputType(InputType.TYPE_CLASS_DATETIME);
        dateTo.setFocusable(false);
        layout.addView(dateTo);
        setupDatePicker(dateTo);

        alertDialog.setPositiveButton("Filter", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

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

    private void setupDatePicker(final EditText editText) {
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd/MM/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                editText.setText(sdf.format(myCalendar.getTime()));
            }
        };
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(SalesActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
}
