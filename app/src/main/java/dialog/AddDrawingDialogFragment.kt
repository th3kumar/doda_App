package dialog

import ViewModel.DrawingViewModel
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.doda.R
import com.google.android.material.textfield.TextInputEditText
import data.Drawing
import java.util.*

class AddDrawingDialogFragment : DialogFragment() {

    private lateinit var imageView: ImageView
    private lateinit var buttonAddImage: Button
    private lateinit var editTextTitle: TextInputEditText

    private var imageUri: Uri? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogView = activity?.layoutInflater?.inflate(R.layout.dialog_add_drawing, null)

        imageView = dialogView?.findViewById(R.id.dialogImageView)!!
        buttonAddImage = dialogView.findViewById(R.id.buttonAddImage)!!
        editTextTitle = dialogView.findViewById(R.id.editTextTitle)!!

        val builder = AlertDialog.Builder(activity)
        builder.setView(dialogView)
            .setTitle(getString(R.string.add_drawing))
            .setPositiveButton(getString(R.string.save)) { _, _ ->
                // Handle positive button click
                val title = editTextTitle.text.toString()
                saveDrawing(title, imageUri)
            }
            .setNegativeButton(getString(R.string.cancel), null)

        buttonAddImage.setOnClickListener {
            // Handle add image button click
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_IMAGE)
        }

        return builder.create()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK) {
            // Get the selected image URI and display it in the ImageView
            imageUri = data?.data
            imageView.setImageURI(imageUri)
            imageView.tag = imageUri
        }
    }

    private fun saveDrawing(title: String, imageUri: Uri?) {
        if (title.isBlank() || imageUri == null) {
            Toast.makeText(requireContext(), "All fields are necessary, Please try again", Toast.LENGTH_SHORT).show()
            return
        }

        val drawing = Drawing(title = title, imageUri = imageUri.toString(), creationTime = Date().time, markerCount = 0)
        val viewModel = ViewModelProvider(requireActivity())[DrawingViewModel::class.java]
        viewModel.insertDrawing(drawing)

        // Fetch all drawings again to refresh the RecyclerView
        //viewModel.getAllDrawings()

        Toast.makeText(requireContext(), "Drawing Saved Successfully", Toast.LENGTH_SHORT).show()

    }



    companion object {
        const val REQUEST_IMAGE = 100
    }
}

