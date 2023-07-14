package com.xujamov.orderfood.ui.login.verify


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.xujamov.orderfood.R
import com.xujamov.orderfood.data.repo.UserRepository
import com.xujamov.orderfood.databinding.FragmentVerifyBinding
import com.xujamov.orderfood.ui.MainActivity
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle


class VerifyFragment(private val verifyId: String) : Fragment(R.layout.fragment_verify) {

    private val binding by viewBinding(FragmentVerifyBinding::bind)
    private val viewModel by lazy { VerifyModel(requireActivity()) }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            viewModel.apply {

                initObservers()
                btnVerify.setOnClickListener {
                    val verifyCode = verifyEditText.text.toString()
                    signInWithCode(verifyId, verifyCode)
                }


            }

        }

    }

    private fun initObservers() {
        binding.apply {
            viewModel.apply {
                isLoading.observe(viewLifecycleOwner) {
                    when (it!!) {
                        UserRepository.LOADING.LOADING -> {
                            btnVerify.startAnimation()
                        }
                        UserRepository.LOADING.DONE -> {

                            btnVerify.revertAnimation {
                                btnVerify.setBackgroundResource(R.drawable.rounded_bg3)
                            }
                        }

                        UserRepository.LOADING.ERROR -> {
                            btnVerify.revertAnimation {
                                btnVerify.setBackgroundResource(R.drawable.rounded_bg3)
                            }
                        }
                    }

                }

                isSignIn.observe(viewLifecycleOwner) {
                    if (it) {
                        MotionToast.createColorToast(
                            requireActivity(),
                            getString(R.string.success),
                            getString(R.string.login_success),
                            MotionToastStyle.SUCCESS,
                            MotionToast.GRAVITY_TOP or MotionToast.GRAVITY_CENTER,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(requireContext(), R.font.circular)
                        )

                        val intent = Intent(context, MainActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    } else {
                        MotionToast.createColorToast(
                            requireActivity(),
                            getString(R.string.error),
                            getString(R.string.wrong_email_password),
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_TOP or MotionToast.GRAVITY_CENTER,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(requireContext(), R.font.circular)
                        )
                    }

                }

                isCodeValid.observe(viewLifecycleOwner) {
                    if (it.not())

                        MotionToast.createColorToast(
                            requireActivity(),
                            getString(R.string.error),
                            getString(R.string.complete_not_entered_info),
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_TOP or MotionToast.GRAVITY_CENTER,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(requireContext(), R.font.circular)
                        )

                }
            }
        }
    }

}