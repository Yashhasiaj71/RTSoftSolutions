package com.example.rtsoftsolutions;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.example.rtsoftsolutions.Models.FeesTransaction;
import com.example.rtsoftsolutions.Models.Student;
import com.github.mikephil.charting.charts.Chart;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PdfExporter {

    public static void exportFeesReport(Context ctx, Chart<?> chart, String summary) {
        try {
            Bitmap chartBitmap = chart.getChartBitmap();
            PdfDocument document = new PdfDocument();
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create(); // A4
            PdfDocument.Page page = document.startPage(pageInfo);
            Canvas canvas = page.getCanvas();

            Paint titlePaint = new Paint();
            titlePaint.setTextSize(20f);
            titlePaint.setFakeBoldText(true);
            canvas.drawText("Fees Report", 40, 50, titlePaint);

            if (chartBitmap != null) {
                Bitmap scaled = Bitmap.createScaledBitmap(chartBitmap, 515, 300, true);
                canvas.drawBitmap(scaled, 40, 80, null);
            }

            Paint textPaint = new Paint();
            textPaint.setTextSize(14f);
            canvas.drawText(summary, 40, 420, textPaint);

            document.finishPage(page);

            String time = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "RTReports");
            if (!dir.exists()) dir.mkdirs();
            File out = new File(dir, "report_" + time + ".pdf");
            FileOutputStream fos = new FileOutputStream(out);
            document.writeTo(fos);
            document.close();
            fos.close();

            // Open the PDF via FileProvider
            try {
                Uri uri = FileProvider.getUriForFile(ctx, ctx.getPackageName() + ".provider", out);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "application/pdf");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                ctx.startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(ctx, "PDF saved to: " + out.getAbsolutePath(), Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            Toast.makeText(ctx, "Export failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    public static void generateFeeReceipt(Context ctx, Student student, FeesTransaction transaction) {
        try {
            PdfDocument document = new PdfDocument();
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create(); // A4
            PdfDocument.Page page = document.startPage(pageInfo);
            Canvas canvas = page.getCanvas();
            
            // Set background
            Paint backgroundPaint = new Paint();
            backgroundPaint.setColor(Color.WHITE);
            canvas.drawRect(0, 0, 595, 842, backgroundPaint);
            
            // Header
            Paint headerPaint = new Paint();
            headerPaint.setTextSize(24f);
            headerPaint.setFakeBoldText(true);
            headerPaint.setColor(Color.BLACK);
            canvas.drawText("RT Soft Solutions", 40, 60, headerPaint);
            
            Paint subtitlePaint = new Paint();
            subtitlePaint.setTextSize(16f);
            subtitlePaint.setColor(Color.GRAY);
            canvas.drawText("Fee Receipt", 40, 85, subtitlePaint);
            
            // Receipt details
            Paint labelPaint = new Paint();
            labelPaint.setTextSize(12f);
            labelPaint.setFakeBoldText(true);
            labelPaint.setColor(Color.BLACK);
            
            Paint valuePaint = new Paint();
            valuePaint.setTextSize(12f);
            valuePaint.setColor(Color.BLACK);
            
            Paint linePaint = new Paint();
            linePaint.setColor(Color.LTGRAY);
            linePaint.setStrokeWidth(1f);
            
            int y = 120;
            int leftMargin = 40;
            int rightMargin = 300;
            
            // Receipt number
            canvas.drawText("Receipt No:", leftMargin, y, labelPaint);
            canvas.drawText(transaction.getTransactionId(), rightMargin, y, valuePaint);
            y += 25;
            
            // Date
            canvas.drawText("Date:", leftMargin, y, labelPaint);
            canvas.drawText(transaction.getDate(), rightMargin, y, valuePaint);
            y += 25;
            
            // Student details
            canvas.drawText("Student Name:", leftMargin, y, labelPaint);
            canvas.drawText(student.name, rightMargin, y, valuePaint);
            y += 25;
            
            canvas.drawText("Phone:", leftMargin, y, labelPaint);
            canvas.drawText(student.phoneNo, rightMargin, y, valuePaint);
            y += 25;
            
            canvas.drawText("Course:", leftMargin, y, labelPaint);
            canvas.drawText(student.selectedCourseName, rightMargin, y, valuePaint);
            y += 25;
            
            canvas.drawText("Batch:", leftMargin, y, labelPaint);
            canvas.drawText(student.selectedBatchName, rightMargin, y, valuePaint);
            y += 25;
            
            // Separator line
            y += 10;
            canvas.drawLine(leftMargin, y, 555, y, linePaint);
            y += 20;
            
            // Payment details
            canvas.drawText("Payment Details", leftMargin, y, labelPaint);
            y += 25;
            
            canvas.drawText("Payment Mode:", leftMargin, y, labelPaint);
            canvas.drawText(transaction.getPaymentMode(), rightMargin, y, valuePaint);
            y += 25;
            
            canvas.drawText("Amount Paid:", leftMargin, y, labelPaint);
            canvas.drawText("₹" + transaction.getAmount(), rightMargin, y, valuePaint);
            y += 25;
            
            canvas.drawText("Total Fees:", leftMargin, y, labelPaint);
            canvas.drawText("₹" + student.totalFees, rightMargin, y, valuePaint);
            y += 25;
            
            canvas.drawText("Previously Paid:", leftMargin, y, labelPaint);
            canvas.drawText("₹" + (student.paidFees - transaction.getAmount()), rightMargin, y, valuePaint);
            y += 25;
            
            canvas.drawText("Remaining Fees:", leftMargin, y, labelPaint);
            canvas.drawText("₹" + student.remainingFees, rightMargin, y, valuePaint);
            y += 30;
            
            // Separator line
            canvas.drawLine(leftMargin, y, 555, y, linePaint);
            y += 20;
            
            // Footer
            Paint footerPaint = new Paint();
            footerPaint.setTextSize(10f);
            footerPaint.setColor(Color.GRAY);
            canvas.drawText("Thank you for your payment!", leftMargin, y, footerPaint);
            y += 15;
            canvas.drawText("For any queries, please contact us.", leftMargin, y, footerPaint);
            y += 15;
            canvas.drawText("RT Soft Solutions - Empowering Education", leftMargin, y, footerPaint);
            
            document.finishPage(page);
            
            // Generate filename
            String time = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "RTReceipts");
            if (!dir.exists()) dir.mkdirs();
            File out = new File(dir, "receipt_" + student.name.replace(" ", "_") + "_" + time + ".pdf");
            FileOutputStream fos = new FileOutputStream(out);
            document.writeTo(fos);
            document.close();
            fos.close();
            
            // Open the PDF
            try {
                Uri uri = FileProvider.getUriForFile(ctx, ctx.getPackageName() + ".provider", out);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "application/pdf");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                ctx.startActivity(intent);
                Toast.makeText(ctx, "Receipt generated successfully!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(ctx, "Receipt saved to: " + out.getAbsolutePath(), Toast.LENGTH_LONG).show();
            }
            
        } catch (IOException e) {
            Toast.makeText(ctx, "Receipt generation failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
