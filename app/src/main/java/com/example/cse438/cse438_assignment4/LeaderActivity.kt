package com.example.cse438.cse438_assignment4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_leader.*import com.example.cse438.cse438_assignment4.util.User
import com.google.firebase.database.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import androidx.core.view.GestureDetectorCompat

class LeaderActivity : AppCompatActivity() {

    var users: User? = null
    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private var leaderList = ArrayList<User>()

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var mDetector: GestureDetectorCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leader)

        mDetector = GestureDetectorCompat(this, MyGestureListener())
        database = Firebase.database.reference

        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val leadersQuery = database.child("users").child(uid)
            .orderByChild("chips")

//        leadersQuery.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                for (postSnapshot in dataSnapshot.children) {
//                    postSnapshot.g
//                    leaderList.add(postSnapshot.)
//                }
//            }
//            override fun onCancelled(databaseError: DatabaseError) {
//                // Getting Post failed, log a message
//            }
//        }


//
//        val rootRef = FirebaseDatabase.getInstance().reference
//        val uidRef = rootRef.child("users").child(uid)
//        val valueEventListener = object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                val user = dataSnapshot.getValue(User::class.java)
//                chips = user!!.chips
//                chips_amount.text = chips.toString()
////                Log.d(TAG, "Lat/Lng: " + user!!.userLocation!!.lat + ", " + user.userLocation!!.lng);
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
////                Log.d(TAG, databaseError.message) //Don't ignore errors!
//            }
//        }
//        uidRef.addListenerForSingleValueEvent(valueEventListener)

        val leaderList = generateList(250)

        recycler_view.adapter = LeaderAdapter(leaderList)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)
    }

    private fun generateList(size: Int): List<LeaderItem> {

        val list = ArrayList<LeaderItem>()

        for (i in 0 until size) {

            val item = LeaderItem("User has 0 chips")
            list += item
        }

        return list
    }

    override fun onTouchEvent(event: MotionEvent) : Boolean {
        mDetector.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    private inner class MyGestureListener : GestureDetector.SimpleOnGestureListener() {

        private var swipedistance = 150

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            finish()
            return true
        }
    }
}
