package com.example.cse438.cse438_assignment4

import android.content.Intent
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import android.view.View
import android.widget.ImageView
import android.view.ViewGroup
import com.example.cse438.cse438_assignment4.util.CardRandomizer
import kotlinx.android.synthetic.main.activity_main.*
import java.util.Random
import androidx.core.view.GestureDetectorCompat
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.animation.LinearInterpolator
import android.animation.ObjectAnimator
import android.animation.AnimatorSet
import android.util.DisplayMetrics
import kotlinx.android.synthetic.main.activity_main.*
import android.view.DragEvent
import android.widget.LinearLayout
import android.R.attr.shape
import android.annotation.TargetApi
import android.app.Activity
import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View.OnDragListener
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.cse438.cse438_assignment4.fragments.SignupFragment
import com.example.cse438.cse438_assignment4.util.User
import com.google.firebase.database.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.w3c.dom.CharacterData

class MainActivity : AppCompatActivity() {

    var users: User? = null
    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var mDetector: GestureDetectorCompat
    private var height: Int = 0
    private var width: Int = 0
    private lateinit var cardView: View
    private var xLocPlayer: Float = 0f
    private var yLocPlayer: Float = 0f
    private var xLocComp: Float = 0f
    private var yLocComp: Float = 0f
    private var allImageViews = ArrayList<ImageView>()
    private var index: Int = 0
    private var chips: Int = 0
    private var myBet: Int = 0
    private var compHandValue: Int = 0
    private var myHandValue: Int = 0
    private var saveCardID: Int = 0
    private var practiceGame: Boolean = false
    private var gameStarted: Boolean = false
    private var computerTurn: Boolean = false
    private var myTurn: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        cardView = draw_card
        mDetector = GestureDetectorCompat(this, MyGestureListener())

//        mAuth = FirebaseAuth.getInstance()
//        database = FirebaseDatabase.getInstance().reference
//
//        fun currentUserReference(): DatabaseReference =
//            database.child("users").child(mAuth.currentUser!!.uid)
//
//        val uid = FirebaseAuth.getInstance().uid?: ""
//        FirebaseDatabase.getInstance().getReference("/users/$uid/").setValue(user)

//        var ref = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().currentUser!!.uid)
//
//        val menuListener = object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                user = dataSnapshot.getValue() as User
//                textView.text = user?.getName()
//            }
//            override fun onCancelled(databaseError: DatabaseError) {
//                // handle error
//            }
//        }
//        ref.addListenerForSingleValueEvent(menuListener)

        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val rootRef = FirebaseDatabase.getInstance().reference
        val uidRef = rootRef.child("users").child(uid)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                chips = user!!.chips
                chips_amount.text = chips.toString()
                player.text = user!!.email.toString()
//                Log.d(TAG, "Lat/Lng: " + user!!.userLocation!!.lat + ", " + user.userLocation!!.lng);
            }

            override fun onCancelled(databaseError: DatabaseError) {
//                Log.d(TAG, databaseError.message) //Don't ignore errors!
            }
        }
        uidRef.addListenerForSingleValueEvent(valueEventListener)

        val metrics = this.resources.displayMetrics
        this.height = metrics.heightPixels
        this.width = metrics.widthPixels

        chips_amount.text = chips.toString()

        allImageViews.add(draw_card4)
        allImageViews.add(draw_card5)
        allImageViews.add(draw_card6)
        allImageViews.add(draw_card7)
        allImageViews.add(draw_card8)
        allImageViews.add(draw_card9)
        allImageViews.add(draw_card10)
        allImageViews.add(draw_card11)
        allImageViews.add(draw_card12)
        allImageViews.add(draw_card13)
        allImageViews.add(draw_card14)
        allImageViews.add(draw_card15)
        allImageViews.add(draw_card16)
        allImageViews.add(draw_card17)
        allImageViews.add(draw_card18)
        allImageViews.add(draw_card19)
        allImageViews.add(draw_card20)
        allImageViews.add(draw_card21)
        allImageViews.add(draw_card22)
        allImageViews.add(draw_card23)
        allImageViews.add(draw_card24)
        allImageViews.add(draw_card25)
    }

    fun placeBet(view: View) {
        if(bet_amt.text.toString().toInt() > chips) {
            notification.text = "Not enough chips :("
        }
        else if(bet_amt.text.toString().toInt() < 0) {
            notification.text = "Bet Invalid"
        }
        else {
            myBet = bet_amt.text.toString().toInt()
            chips_amount.text = (chips - myBet).toString()
            setChips(chips-myBet)
            betView.visibility = View.INVISIBLE
            notification.text = "Bet Placed"
            gameStarted = true
            myTurn = true
        }
    }

    fun moveTo(targetX: Float, targetY: Float) {

        val animSetXY = AnimatorSet()

        val x = ObjectAnimator.ofFloat(
            cardView,
            "translationX",
            cardView.translationX,
            targetX
        )

        val y = ObjectAnimator.ofFloat(
            cardView,
            "translationY",
            cardView.translationY,
            targetY
        )

        animSetXY.playTogether(x, y)
        animSetXY.duration = 800
        animSetXY.start()
    }

    override fun onResume() {
        super.onResume()

        val randomizer: CardRandomizer = CardRandomizer()
        var cardList: ArrayList<Int> = randomizer.getIDs(this) as ArrayList<Int>
        val rand: Random = Random()
        val r: Int = rand.nextInt(cardList.size)
        val id: Int = cardList.get(r)
        val name: String = resources.getResourceEntryName(id)

        saveCardID = getCardID()
        compHandValue += getCardValue(saveCardID)
        cardView = face_down_card
        moveTo(this@MainActivity.width / 3f, -this@MainActivity.height / 3f)

        saveCardID = getCardID()
        draw_card.setImageResource(saveCardID)
        compHandValue += getCardValue(saveCardID)
        cardView = draw_card
        moveTo(this@MainActivity.width / 3f - 60f, -this@MainActivity.height / 3f)
        xLocComp = this@MainActivity.width / 3f - 60f
        yLocComp = -this@MainActivity.height / 3f

        saveCardID = getCardID()
        draw_card2.setImageResource(saveCardID)
        myHandValue += getCardValue(saveCardID)
        cardView = draw_card2
        moveTo(-this@MainActivity.width / 3f, this@MainActivity.height / 3f - 80f)

        saveCardID = getCardID()
        draw_card3.setImageResource(saveCardID)
        myHandValue += getCardValue(saveCardID)
        cardView = draw_card3
        moveTo(-this@MainActivity.width / 3f + 60f, this@MainActivity.height / 3f - 80f)
        xLocPlayer = -this@MainActivity.width / 3f + 60f
        yLocPlayer = this@MainActivity.height / 3f - 80f

    }

    fun getCardID(): Int {
        val randomizer: CardRandomizer = CardRandomizer()
        var cardList: ArrayList<Int> = randomizer.getIDs(this) as ArrayList<Int>
        val rand: Random = Random()
        val r: Int = rand.nextInt(cardList.size)
        val id: Int = cardList.get(r)
        val name: String = resources.getResourceEntryName(id)

        return id
    }

    fun getCardValue(cardID: Int): Int {
        if(resources.getResourceEntryName(cardID) == "clubs2") {
            return 2
        }
        else if(resources.getResourceEntryName(cardID) == "clubs3") {
            return 3
        }
        else if(resources.getResourceEntryName(cardID) == "clubs4") {
            return 4
        }
        else if(resources.getResourceEntryName(cardID) == "clubs5") {
            return 5
        }
        else if(resources.getResourceEntryName(cardID) == "clubs6") {
            return 6
        }
        else if(resources.getResourceEntryName(cardID) == "clubs7") {
            return 7
        }
        else if(resources.getResourceEntryName(cardID) == "clubs8") {
            return 8
        }
        else if(resources.getResourceEntryName(cardID) == "clubs9") {
            return 9
        }
        else if(resources.getResourceEntryName(cardID) == "clubs10") {
            return 10
        }
        else if(resources.getResourceEntryName(cardID) == "clubs_jack") {
            return 10
        }
        else if(resources.getResourceEntryName(cardID) == "clubs_queen") {
            return 11
        }
        else if(resources.getResourceEntryName(cardID) == "clubs_king") {
            return 12
        }
        else if(resources.getResourceEntryName(cardID) == "clubs_ace") {
            if(myHandValue > 10) {
                return 1
            }
            else if(compHandValue > 10) {
                return 1
            }
            return 11
        }
        else if(resources.getResourceEntryName(cardID) == "diamonds2") {
            return 2
        }
        else if(resources.getResourceEntryName(cardID) == "diamonds3") {
            return 3
        }
        else if(resources.getResourceEntryName(cardID) == "diamonds4") {
            return 4
        }
        else if(resources.getResourceEntryName(cardID) == "diamonds5") {
            return 5
        }
        else if(resources.getResourceEntryName(cardID) == "diamonds6") {
            return 6
        }
        else if(resources.getResourceEntryName(cardID) == "diamonds7") {
            return 7
        }
        else if(resources.getResourceEntryName(cardID) == "diamonds8") {
            return 8
        }
        else if(resources.getResourceEntryName(cardID) == "diamonds9") {
            return 9
        }
        else if(resources.getResourceEntryName(cardID) == "diamonds10") {
            return 9
        }
        else if(resources.getResourceEntryName(cardID) == "diamonds_jack") {
            return 10
        }
        else if(resources.getResourceEntryName(cardID) == "diamonds_queen") {
            return 10
        }
        else if(resources.getResourceEntryName(cardID) == "diamonds_king") {
            return 10
        }
        else if(resources.getResourceEntryName(cardID) == "diamonds_ace") {
            if(myHandValue > 10) {
                return 1
            }
            else if(compHandValue > 10) {
                return 1
            }
            return 11
        }
        else if(resources.getResourceEntryName(cardID) == "spades2") {
            return 2
        }
        else if(resources.getResourceEntryName(cardID) == "spades3") {
            return 3
        }
        else if(resources.getResourceEntryName(cardID) == "spades4") {
            return 4
        }
        else if(resources.getResourceEntryName(cardID) == "spades5") {
            return 5
        }
        else if(resources.getResourceEntryName(cardID) == "spades6") {
            return 6
        }
        else if(resources.getResourceEntryName(cardID) == "spades7") {
            return 7
        }
        else if(resources.getResourceEntryName(cardID) == "spades8") {
            return 8
        }
        else if(resources.getResourceEntryName(cardID) == "spades9") {
            return 9
        }
        else if(resources.getResourceEntryName(cardID) == "spades10") {
            return 10
        }
        else if(resources.getResourceEntryName(cardID) == "spades_jack") {
            return 10
        }
        else if(resources.getResourceEntryName(cardID) == "spades_queen") {
            return 10
        }
        else if(resources.getResourceEntryName(cardID) == "spades_king") {
            return 10
        }
        else if(resources.getResourceEntryName(cardID) == "spades_ace") {
            if(myHandValue > 10) {
                return 1
            }
            else if(compHandValue > 10) {
                return 1
            }
            return 11
        }
        else if(resources.getResourceEntryName(cardID) == "hearts2") {
            return 2
        }
        else if(resources.getResourceEntryName(cardID) == "hearts3") {
            return 3
        }
        else if(resources.getResourceEntryName(cardID) == "hearts4") {
            return 4
        }
        else if(resources.getResourceEntryName(cardID) == "hearts5") {
            return 5
        }
        else if(resources.getResourceEntryName(cardID) == "hearts6") {
            return 6
        }
        else if(resources.getResourceEntryName(cardID) == "hearts7") {
            return 7
        }
        else if(resources.getResourceEntryName(cardID) == "hearts8") {
            return 8
        }
        else if(resources.getResourceEntryName(cardID) == "hearts9") {
            return 9
        }
        else if(resources.getResourceEntryName(cardID) == "hearts10") {
            return 10
        }
        else if(resources.getResourceEntryName(cardID) == "hearts_jack") {
            return 10
        }
        else if(resources.getResourceEntryName(cardID) == "hearts_queen") {
            return 10
        }
        else if(resources.getResourceEntryName(cardID) == "hearts_king") {
            return 10
        }
        else if(resources.getResourceEntryName(cardID) == "hearts_ace") {
            if(myHandValue > 10) {
                return 1
            }
            else if(compHandValue > 10) {
                return 1
            }
            return 11
        }else {
            return 0
        }
    }

    override fun onTouchEvent(event: MotionEvent) : Boolean {
        mDetector.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    private inner class MyGestureListener : GestureDetector.SimpleOnGestureListener() {

        private var swipedistance = 150

        override fun onDoubleTap(e: MotionEvent?): Boolean {
            if(gameStarted == false || computerTurn == true) {
                return false
            }

            else {
                saveCardID = getCardID()
                allImageViews[index].setImageResource(saveCardID)
                myHandValue += getCardValue(saveCardID)
                if(myHandValue > 21) {
                    notification.text = "Bust"
                    myTurn = false
                    gameStarted = false
                    val handler = Handler()
                    handler.postDelayed({
                        finish();
                        startActivity(getIntent());
                    }, 1000)
                }
                cardView = allImageViews[index]
                moveTo(xLocPlayer + 60f, this@MainActivity.height / 3f - 80f)
                xLocPlayer = xLocPlayer + 60f
                index++
                return true
            }
        }

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            if(myTurn == true) {
                myTurn = false
                computerTurn = true

                if(computerTurn == true) {
                    notification.text = "Computer's Turn"
                    while(compHandValue < 17) {
                            saveCardID = getCardID()
                            allImageViews[index].setImageResource(saveCardID)
                            compHandValue += getCardValue(saveCardID)
                            cardView = allImageViews[index]
                            moveTo(xLocComp - 60f, -this@MainActivity.height / 3f)
                            xLocComp = xLocComp - 60f
                            index++
                    }

                    if(compHandValue < myHandValue || compHandValue > 21) {
                        chips_amount.text = (chips+myBet*2).toString()
                        notification.text = "You Win"
                        setChips(chips+(myBet*2))

                        val handler = Handler()
                        handler.postDelayed({
                            finish()
                            startActivity(getIntent())
                        }, 1000)
                    }
                    else {
                        if(chips == 0) {
                            chips = 10
                            chips_amount.text = "10"
                            setChips(10)
                        }
                        notification.text = "You Lose"
                        val handler = Handler()
                        handler.postDelayed({
                            finish()
                            startActivity(getIntent())
                        }, 1000)
                    }
                }

                return true
            }
            return false
        }
    }

    fun setChips(chipAmt: Int) {
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val email = FirebaseAuth.getInstance().currentUser!!.email
        FirebaseDatabase.getInstance().getReference("users").child(uid).child("chips").setValue(chipAmt)
    }

    fun seeLeader(view: View) {
        val intent = Intent(this, LeaderActivity::class.java)
        startActivity(intent)
    }

    fun logout(view: View) {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}
