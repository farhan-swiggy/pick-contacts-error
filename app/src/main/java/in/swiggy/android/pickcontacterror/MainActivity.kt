package `in`.swiggy.android.pickcontacterror

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_GET_CONTACT = 5
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun pickContact(view: View) {
        val contactPickerIntent = Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
        startActivityForResult(contactPickerIntent, REQUEST_GET_CONTACT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_GET_CONTACT) {
            var cursor: Cursor? = null
            try {
                val uri = data?.data
                uri?.let {
                    val projection = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone
                            .DISPLAY_NAME)
                    cursor = contentResolver?.query(uri, projection, null, null, null)
                    if (cursor?.moveToFirst() == true) {
                        val phoneIndex = cursor?.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        val nameIndex = cursor?.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                        val name = nameIndex?.let {
                            cursor?.getString(nameIndex)
                        }
                        val phoneNo = phoneIndex?.let {
                            cursor?.getString(phoneIndex)
                        }
                        findViewById<TextView>(R.id.contact).text = "$name\n$phoneNo"
                    }
                }
            } finally {
                cursor?.close()
            }
        }
    }
}
