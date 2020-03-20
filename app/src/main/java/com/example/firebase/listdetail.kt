package com.example.firebase

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase

/**
 * A simple [Fragment] subclass.
 */
class listdetail : Fragment() {
    private var titleTextView : String? = null
    private var detailTextView : String? = null
    private var image : String? = null

    private var btn_comment : Button?= null
    lateinit var comment_food : EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_listdetail, container, false)
        // Inflate the layout for this fragment

        var layout_titleTextView : TextView = view.findViewById<TextView>(R.id.detail_title)
        var layout_detailTextView : TextView = view.findViewById<TextView>(R.id.detail_description)
        var layout_image : ImageView = view.findViewById<ImageView>(R.id.detail_img)

        // ตรงนี้
        layout_titleTextView.setText(titleTextView)
        layout_detailTextView.setText(detailTextView)
        Glide.with(this)
            .load(image)
            .into(layout_image);

        comment_food = view.findViewById<EditText>(R.id.comment_food)
        btn_comment = view.findViewById<Button>(R.id.btn_comment)
        btn_comment!!.setOnClickListener {
            val  builder: AlertDialog.Builder = AlertDialog.Builder(this.context)
            builder.setMessage("Do you want to comment?")
            builder.setPositiveButton("Comment",
                DialogInterface.OnClickListener{ dialog, id ->
                    saveCommentFood()
                })
            builder.setNegativeButton("Cancel",
                DialogInterface.OnClickListener{ dialog, which ->
                    dialog.dismiss()
                })
            builder.show()
        }

        return view

    }

    private fun saveCommentFood(){
        val comment_food_detail = comment_food!!.text.toString().trim()

        if(comment_food_detail.isEmpty()){
            comment_food!!.error = "................"
            return
        }

        val ref = FirebaseDatabase.getInstance().getReference("Food")
        val comment_food_detail_id = ref.push().key

        val food = comment_food_detail_id?.let { Food(it, comment_food_detail) }

        comment_food_detail_id?.let {
            ref.child(it).setValue(food).addOnCompleteListener {
                Toast.makeText(context,"Comment Food Success", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun newInstance(title: String,detail: String,image:String): listdetail {
        val fragment = listdetail()
        val bundle = Bundle()
        bundle.putString("titleTextView", title)
        bundle.putString("detailTextView", detail)
        bundle.putString("image", image)
        fragment.setArguments(bundle)
        return fragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bundle = arguments
        if (bundle != null) {
            titleTextView = bundle.getString("titleTextView").toString()
            detailTextView = bundle.getString("detailTextView").toString()
            image = bundle.getString("image").toString()
        }
    }

}
