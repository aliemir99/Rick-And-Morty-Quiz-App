/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.android.navigation.databinding.FragmentGameBinding

class GameFragment : Fragment() {

    private lateinit var viewModel: GameViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentGameBinding>(
                inflater,
                R.layout.fragment_game,
                container,
                false
        )
        binding.correctImage.isVisible = false
        binding.wrongImage.isVisible = false



        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        // Bind this fragment class to the layout
        binding.gameViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setQuestionText(binding.questionText)

        //Click Listeners
        binding.nextBtn.setOnClickListener {
            viewModel.nextQuestion()
            setQuestionText(binding.questionText)
            updateViewValues(viewModel, binding)
            isAllQuestionsCompleted()
        }

        binding.prevBtn.setOnClickListener {
            viewModel.prevQuestion()
            setQuestionText(binding.questionText)
            updateViewValues(viewModel, binding)
            isAllQuestionsCompleted()
        }

        binding.trueBtn.setOnClickListener {
            viewModel.registerAnswer(true)
            updateButtons(binding.trueBtn, binding.falseBtn, false)
            updateViewValues(viewModel, binding)
            isAllQuestionsCompleted()
        }

        binding.falseBtn.setOnClickListener {
            viewModel.registerAnswer(false)
            updateButtons(binding.trueBtn, binding.falseBtn, false)
            updateViewValues(viewModel, binding)
            isAllQuestionsCompleted()
        }



        //Observe when game is finished execute....
        viewModel.eventGamefinished.observe(viewLifecycleOwner, Observer<Boolean>
        { hasFinished -> if (hasFinished) gameFinished() })

        updateTextView(binding.textView)

        return binding.root
    }


    private fun setQuestionText(view: TextView) {
        val quesText = viewModel.questionString.value
        view.text = context?.getText(Integer.parseInt(quesText))
    }

    private fun updateButtons(trueBtn: Button, falseBtn: Button, status: Boolean) {
        trueBtn.isEnabled = status
        falseBtn.isEnabled = status
    }

    private fun gameFinished() {
        val action = GameFragmentDirections.actionGameFragmentToGameWonFragment(
                viewModel.questionsAttempted(), viewModel.userScore)


        NavHostFragment.findNavController(this).navigate(action)

        viewModel.onGameFinishComplete()
    }

    private fun isAllQuestionsCompleted() {
        if (viewModel.isAllQuestionsAttempted()) {
            viewModel.onGameFinish()
        }
    }

    private fun updateViewValues(viewModel: GameViewModel, binding: FragmentGameBinding) {

        updateTextView(binding.textView)

        if (viewModel.attempted.value == true) {
            updateButtons(binding.trueBtn, binding.falseBtn, false)

            if (viewModel.isCorrect.value == true) {

                binding.correctImage.isVisible = true
                binding.wrongImage.isVisible = false

                if (viewModel.actualAnswer.value == true) {
                    binding.trueBtn.isChecked = true
                    binding.falseBtn.isChecked = false
                } else {
                    binding.falseBtn.isChecked = true
                    binding.trueBtn.isChecked = false
                }
            } else {
                binding.wrongImage.isVisible = true
                binding.correctImage.isVisible = false
            }
        } else {
            binding.correctImage.isVisible = false
            binding.wrongImage.isVisible = false

            updateButtons(binding.trueBtn, binding.falseBtn, true)

            binding.trueBtn.isChecked = false
            binding.falseBtn.isChecked = false
        }
    }

    fun updateTextView(view : TextView){
        view.text = viewModel.questionsAttempted().toString().plus(" / ").plus(viewModel.questionAmount)
    }
}
