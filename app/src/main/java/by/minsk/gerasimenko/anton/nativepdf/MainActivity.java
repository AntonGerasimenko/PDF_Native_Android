package by.minsk.gerasimenko.anton.nativepdf;

import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    ImageView pdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pdfView = (ImageView) findViewById(R.id.pdfView);

        display();
    }

    void display() {

        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

        // filePath represent path of Pdf document on storage
        File file = new File("/storage/emulated/0/Download/simple.pdf");
// FileDescriptor for file, it allows you to close file when you are
// done with it
        ParcelFileDescriptor mFileDescriptor = null;
        try {
            mFileDescriptor = ParcelFileDescriptor.open(file,
                    ParcelFileDescriptor.MODE_READ_ONLY);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
// PdfRenderer enables rendering a PDF document
        PdfRenderer mPdfRenderer = null;
        try {
            mPdfRenderer = new PdfRenderer(mFileDescriptor);
        } catch (IOException e) {
            e.printStackTrace();
        }

// Open page with specified index
        PdfRenderer.Page mCurrentPage = mPdfRenderer.openPage(0);
        Bitmap bitmap = Bitmap.createBitmap(mCurrentPage.getWidth(),
                mCurrentPage.getHeight(), Bitmap.Config.ARGB_8888);

// Pdf page is rendered on Bitmap
        mCurrentPage.render(bitmap, null, null,
                PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
// Set rendered bitmap to ImageView (pdfView in my case)
        pdfView.setImageBitmap(bitmap);

        mCurrentPage.close();
        mPdfRenderer.close();
        try {
            mFileDescriptor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
