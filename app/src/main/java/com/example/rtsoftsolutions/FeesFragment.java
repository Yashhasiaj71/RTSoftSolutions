package com.example.rtsoftsolutions;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import com.example.rtsoftsolutions.FeesTransaction;

public class FeesFragment extends Fragment {

    private static final String TAG = "FeesFragment";
    
    // UI Components
    private EditText studentSearchEditText;
    private Button searchButton;
    private CardView studentView;
    private TextView remainingFeesText;
    private EditText amountEditText;
    private ImageButton upiModeButton;
    private ImageButton cashModeButton;
    private ImageButton bankModeButton;
    private ImageButton whatsappButton;
    private ListView searchResultsListView;
    private LinearLayout searchResultsContainer;
    
    // Firebase
    private FirebaseDatabase database;
    private DatabaseReference studentsRef;
    private DatabaseReference transactionsRef;
    
    // Current student data
    private Student currentStudent;
    private String currentStudentId;
    private String selectedPaymentMode = "";
    
    // Search results
    private List<StudentSearchResult> searchResults;
    private ArrayAdapter<StudentSearchResult> searchAdapter;
    
    // Payment modes
    private static final String PAYMENT_MODE_UPI = "UPI";
    private static final String PAYMENT_MODE_CASH = "CASH";
    private static final String PAYMENT_MODE_BANK = "BANK";

    public FeesFragment() {
        // Required empty public constructor
    }

    public static FeesFragment newInstance(String param1, String param2) {
        FeesFragment fragment = new FeesFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Handle arguments if needed
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fees, container, false);
        
        // Initialize Firebase
        database = FirebaseDatabase.getInstance("https://rtsoftsolutions-fc2cf-default-rtdb.firebaseio.com/");
        studentsRef = database.getReference("students");
        transactionsRef = database.getReference("fees_transactions");
        
        // Initialize search results
        searchResults = new ArrayList<>();
        
        // Initialize UI components
        initializeViews(view);
        setupClickListeners();
        setupSearchAdapter();
        
        // Hide search results when clicking outside
        view.setOnClickListener(v -> hideSearchResults());
        
        return view;
    }
    
    private void initializeViews(View view) {
        studentSearchEditText = view.findViewById(R.id.StudentSearch);
        searchButton = view.findViewById(R.id.SearchButton);
        studentView = view.findViewById(R.id.StudentView);
        remainingFeesText = view.findViewById(R.id.RemainingFees);
        amountEditText = view.findViewById(R.id.AmountEditText);
        upiModeButton = view.findViewById(R.id.Upi_mode);
        cashModeButton = view.findViewById(R.id.cash_mode);
        bankModeButton = view.findViewById(R.id.bank_mode);
        whatsappButton = view.findViewById(R.id.WhatsappButton);
        
        // Search results components
        searchResultsContainer = view.findViewById(R.id.SearchResultsContainer);
        searchResultsListView = view.findViewById(R.id.SearchResultsListView);
        
        // Process payment button
        Button processPaymentButton = view.findViewById(R.id.ProcessPaymentButton);
        processPaymentButton.setOnClickListener(v -> processPayment());
        
        // Initially hide student view and search results
        studentView.setVisibility(View.GONE);
        searchResultsContainer.setVisibility(View.GONE);
    }
    
    private void setupClickListeners() {
        // Search button click listener
        searchButton.setOnClickListener(v -> searchStudent());
        
        // Real-time search on text change
        studentSearchEditText.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 2) {
                    searchStudent();
                } else {
                    hideSearchResults();
                }
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
        
        // Payment mode selection
        upiModeButton.setOnClickListener(v -> selectPaymentMode(PAYMENT_MODE_UPI));
        cashModeButton.setOnClickListener(v -> selectPaymentMode(PAYMENT_MODE_CASH));
        bankModeButton.setOnClickListener(v -> selectPaymentMode(PAYMENT_MODE_BANK));
        
        // WhatsApp reminder
        whatsappButton.setOnClickListener(v -> sendWhatsAppReminder());
    }
    
    public void setupSearchAdapter() {
        searchAdapter = new ArrayAdapter<StudentSearchResult>(requireContext(), 
                android.R.layout.simple_list_item_1, searchResults) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (view instanceof TextView) {
                    StudentSearchResult result = searchResults.get(position);
                    ((TextView) view).setText(result.getDisplayText());
                    ((TextView) view).setTextSize(14);
                    ((TextView) view).setPadding(20, 15, 20, 15);
                }
                return view;
            }
        };
        
        searchResultsListView.setAdapter(searchAdapter);
        
        // Handle item selection
        searchResultsListView.setOnItemClickListener((parent, view, position, id) -> {
            StudentSearchResult selectedResult = searchResults.get(position);
            selectStudent(selectedResult.getStudentId());
            hideSearchResults();
        });
    }
    
    private void searchStudent() {
        String searchQuery = studentSearchEditText.getText().toString().trim();
        
        if (searchQuery.length() < 2) {
            hideSearchResults();
            return;
        }
        
        // Show loading
        Toast.makeText(requireContext(), "Searching...", Toast.LENGTH_SHORT).show();
        
        studentsRef.orderByChild("name").startAt(searchQuery).endAt(searchQuery + "\uf8ff")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        searchResults.clear();
                        
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                Student student = snapshot.getValue(Student.class);
                                if (student != null && student.name.toLowerCase().contains(searchQuery.toLowerCase())) {
                                    StudentSearchResult result = new StudentSearchResult(
                                            snapshot.getKey(),
                                            student.name,
                                            student.selectedCourseName,
                                            student.selectedBatchName,
                                            student.phoneNo,
                                            student.remainingFees
                                    );
                                    searchResults.add(result);
                                }
                            }
                        }
                        
                        if (!searchResults.isEmpty()) {
                            showSearchResults();
                        } else {
                            hideSearchResults();
                            Toast.makeText(requireContext(), "No students found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(requireContext(), "Search failed: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Search failed", databaseError.toException());
                    }
                });
    }
    
    private void showSearchResults() {
        searchAdapter.notifyDataSetChanged();
        searchResultsContainer.setVisibility(View.VISIBLE);
    }
    
    private void hideSearchResults() {
        searchResultsContainer.setVisibility(View.GONE);
    }
    
    private void selectStudent(String studentId) {
        // Fetch complete student data
        studentsRef.child(studentId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Student student = dataSnapshot.getValue(Student.class);
                if (student != null) {
                    displayStudentInfo(student, studentId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(requireContext(), "Failed to load student details", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void displayStudentInfo(Student student, String studentId) {
        currentStudent = student;
        currentStudentId = studentId;
        
        // Update student view
        studentView.removeAllViews();
        
        TextView studentInfoText = new TextView(requireContext());
        studentInfoText.setText("Student: " + student.name + "\nCourse: " + student.selectedCourseName + 
                               "\nBatch: " + student.selectedBatchName + "\nPhone: " + student.phoneNo +
                               "\nTotal Fees: ₹" + student.totalFees + "\nPaid Fees: ₹" + student.paidFees);
        studentInfoText.setTextSize(16);
        studentInfoText.setPadding(20, 20, 20, 20);
        studentView.addView(studentInfoText);
        
        // Update remaining fees
        updateRemainingFeesDisplay();
        
        // Show student view
        studentView.setVisibility(View.VISIBLE);
        
        Toast.makeText(requireContext(), "Student selected: " + student.name, Toast.LENGTH_SHORT).show();
    }
    
    private void updateRemainingFeesDisplay() {
        if (currentStudent != null) {
            remainingFeesText.setText("Remaining fees for " + currentStudent.name + " = ₹" + currentStudent.remainingFees);
        }
    }
    
    private void selectPaymentMode(String mode) {
        selectedPaymentMode = mode;
        
        // Reset all button backgrounds
        upiModeButton.setBackgroundResource(android.R.color.transparent);
        cashModeButton.setBackgroundResource(android.R.color.transparent);
        bankModeButton.setBackgroundResource(android.R.color.transparent);
        
        // Highlight selected mode
        switch (mode) {
            case PAYMENT_MODE_UPI:
                upiModeButton.setBackgroundResource(R.color.Accent_green);
                break;
            case PAYMENT_MODE_CASH:
                cashModeButton.setBackgroundResource(R.color.Accent_green);
                break;
            case PAYMENT_MODE_BANK:
                bankModeButton.setBackgroundResource(R.color.Accent_green);
                break;
        }
        
        Toast.makeText(requireContext(), "Payment mode: " + mode, Toast.LENGTH_SHORT).show();
    }
    
    private void processPayment() {
        if (currentStudent == null) {
            Toast.makeText(requireContext(), "Please search for a student first", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (selectedPaymentMode.isEmpty()) {
            Toast.makeText(requireContext(), "Please select a payment mode", Toast.LENGTH_SHORT).show();
            return;
        }
        
        String amountStr = amountEditText.getText().toString().trim();
        if (amountStr.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter amount", Toast.LENGTH_SHORT).show();
            return;
        }
        
        int amount;
        try {
            amount = Integer.parseInt(amountStr);
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Please enter a valid amount", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (amount <= 0) {
            Toast.makeText(requireContext(), "Amount must be greater than 0", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (amount > currentStudent.remainingFees) {
            Toast.makeText(requireContext(), "Amount cannot exceed remaining fees", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Create transaction record
        FeesTransaction transaction = new FeesTransaction(currentStudentId, currentStudent.name, amount, selectedPaymentMode);
        
        // Save transaction to Firebase
        String transactionKey = transactionsRef.push().getKey();
        transactionsRef.child(transactionKey).setValue(transaction)
                .addOnSuccessListener(aVoid -> {
                    // Update student's paid fees
                    updateStudentFees(amount);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Payment failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Payment failed", e);
                });
    }
    
    private void updateStudentFees(int amountPaid) {
        int newPaidFees = currentStudent.paidFees + amountPaid;
        int newRemainingFees = currentStudent.totalFees - newPaidFees;
        
        // Update student record
        studentsRef.child(currentStudentId).child("paidFees").setValue(newPaidFees);
        studentsRef.child(currentStudentId).child("remainingFees").setValue(newRemainingFees)
                .addOnSuccessListener(aVoid -> {
                    // Update local data
                    currentStudent.paidFees = newPaidFees;
                    currentStudent.remainingFees = newRemainingFees;
                    
                    // Update display
                    updateRemainingFeesDisplay();
                    
                    // Clear form
                    amountEditText.setText("");
                    selectedPaymentMode = "";
                    
                    // Reset payment mode buttons
                    upiModeButton.setBackgroundResource(android.R.color.transparent);
                    cashModeButton.setBackgroundResource(android.R.color.transparent);
                    bankModeButton.setBackgroundResource(android.R.color.transparent);
                    
                    Toast.makeText(requireContext(), "Payment processed successfully! Amount: ₹" + amountPaid, Toast.LENGTH_LONG).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireContext(), "Failed to update student fees: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Failed to update student fees", e);
                });
    }
    
    private void sendWhatsAppReminder() {
        if (currentStudent == null) {
            Toast.makeText(requireContext(), "Please search for a student first", Toast.LENGTH_SHORT).show();
            return;
        }
        
        String phoneNo = currentStudent.phoneNo;
        if (phoneNo.startsWith("+91")) {
            phoneNo = phoneNo.substring(3); // Remove +91 prefix
        }
        
        String message = "Dear " + currentStudent.name + ", your remaining fees (₹" + currentStudent.remainingFees + 
                        ") is pending. Kindly pay it by the due date. - Team RT Soft Solutions";

                Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://wa.me/91" + phoneNo + "?text=" + Uri.encode(message)));

        try {
                    startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(requireContext(), "WhatsApp not installed", Toast.LENGTH_SHORT).show();
        }
    }
    
    // Inner class to represent search results
    private static class StudentSearchResult {
        private String studentId;
        private String name;
        private String courseName;
        private String batchName;
        private String phoneNo;
        private int remainingFees;
        
        public StudentSearchResult(String studentId, String name, String courseName, 
                                 String batchName, String phoneNo, int remainingFees) {
            this.studentId = studentId;
            this.name = name;
            this.courseName = courseName;
            this.batchName = batchName;
            this.phoneNo = phoneNo;
            this.remainingFees = remainingFees;
        }
        
        public String getStudentId() { return studentId; }
        public String getName() { return name; }
        public String getCourseName() { return courseName; }
        public String getBatchName() { return batchName; }
        public String getPhoneNo() { return phoneNo; }
        public int getRemainingFees() { return remainingFees; }
        
        public String getDisplayText() {
            return name + " - " + courseName + " (" + batchName + ")\n" +
                   "Phone: " + phoneNo + " | Remaining: ₹" + remainingFees;
        }
        
        @Override
        public String toString() {
            return getDisplayText();
        }
    }
}