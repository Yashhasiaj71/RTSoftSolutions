package com.example.rtsoftsolutions;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import androidx.core.content.FileProvider;

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
}
