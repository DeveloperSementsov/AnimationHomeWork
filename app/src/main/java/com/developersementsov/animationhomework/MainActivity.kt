package com.developersementsov.animationhomework

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.developersementsov.animationhomework.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var animateGlass1: ObjectAnimator
    private lateinit var animateGlass2: ObjectAnimator

    // Generate a random number to choose a random side
    private var side = (Math.random() * 2).toInt()
    private var isSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        startGame()
    }

    private fun startGame() {
        with(binding) {
            // Creating Animations using ObjectAnimators
            animateGlass1 = ObjectAnimator.ofFloat(glass1, "translationY", 1300f)
            animateGlass1.duration = 4000
            animateGlass2 = ObjectAnimator.ofFloat(glass2, "translationY", 1300f)
            animateGlass2.duration = 4000

            setListeners()

            // Starting the Animations for the First Time
            animateGlass1.start()
            animateGlass2.start()
        }
    }

    private fun setListeners() {
        with(binding) {
            button.setOnClickListener {
                isSelected = true
                continueGame()
                button.visibility = View.GONE
            }

            // Adding onClick Listeners to check the correct side
            glass1.setOnClickListener {
                if (side == 1) {
                    // Wrong Side
                    // Change the image to Broken Glass
                    glass1.setImageResource(R.drawable.broken_glass)

                    // Stop the Animation
                    animateGlass1.pause()
                    animateGlass2.pause()
                    isSelected = false
                    animateGlass1.removeAllListeners()
                    showToast(true)
                    showRepeatButton()
                } else {
                    // Correct Side
                    // Change isSelected to True
                    isSelected = true
                    showToast(false)
                    rotation()
                }
            }
            glass2.setOnClickListener {
                if (side == 0) {
                    // Wrong Side
                    // Change the image to Broken Glass
                    glass2.setImageResource(R.drawable.broken_glass)
                    // Stop the Animation
                    animateGlass1.pause()
                    animateGlass2.pause()
                    isSelected = false
                    animateGlass1.removeAllListeners()
                    showToast(true)
                    showRepeatButton()
                } else {
                    // Correct Side
                    // Change isSelected to True
                    isSelected = true
                    showToast(false)
                    rotation()
                }
            }

            // Adding Animation End Listener
            animateGlass1.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    // Check any of the glasses are selected
                    if (isSelected) {
                        continueGame()
                    } else {
                        // No glasses are Selected
                        glass1.setImageResource(R.drawable.broken_glass)
                        glass2.setImageResource(R.drawable.broken_glass)
                        // Stop the Animation
                        animateGlass1.cancel()
                        animateGlass2.cancel()
                        showToast(true)
                        showRepeatButton()
                    }
                }
            })
        }
    }

    private fun continueGame() {
        isSelected = false
        with(binding) {
            // Place the glasses again in the top
            restoreGlass()
            glass1.y = 0f
            glass2.y = 0f
            // new random side
            side = (Math.random() * 2).toInt()
            // start the animations
            animateGlass1.start()
            animateGlass2.start()
        }
    }

    private fun restoreGlass() {
        with(binding) {
            glass1.setImageResource(R.drawable.glass)
            glass2.setImageResource(R.drawable.glass)
        }

    }

    private fun showToast(isFail: Boolean) {
        val text = if (isFail) "Ops.. Try Again!" else "Lucky :)"
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun showRepeatButton() {
        binding.button.visibility = View.VISIBLE
    }

    private fun rotation() {
        with(binding) {
            val rotationGlass1 = ObjectAnimator.ofFloat(glass1, View.ROTATION, 0f, 360f)
            rotationGlass1.duration = 1000
            val rotationGlass2 = ObjectAnimator.ofFloat(glass2, "rotation", 0f, 360f)
            rotationGlass2.duration = 1000
            rotationGlass1.start()
            rotationGlass2.start()

            rotationGlass1.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    rotationGlass1.cancel()
                    rotationGlass2.cancel()
                    continueGame()
                }
            })
        }
    }
}