package com.doris.bba_android.ui.home.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.doris.bba_android.R
import com.doris.bba_android.databinding.FragmentProfileBinding
import com.doris.bba_android.model.BabyModel
import com.doris.bba_android.model.UserModel
import com.doris.bba_android.network.users.BabiesManager
import com.doris.bba_android.network.users.UserManager
import com.doris.bba_android.ui.common.DialogUi
import com.doris.bba_android.ui.home.RegisterBabyActivity
import com.doris.bba_android.ui.home.ui.adapters.BabyAdapter
import com.doris.bba_android.utils.Constants
import com.doris.bba_android.utils.SharedPreferencesManager

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var _dialog: DialogUi? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        actionButtons()
        initialGetProfileData()

    }

    private fun initialGetProfileData() {
        try {
            setupAlert(Constants.STATUS_LOADING) {}
            val userManager = UserManager()
            val prefs = SharedPreferencesManager(requireContext())
            val idUser = prefs.getPref("idUser", "empty").toString()
            userManager.getOneUserColl(idUser) {
                if (it != null) {
                    initialPrintInfoUI(it)
                    initialGetBabiesData(idUser)
                }
            }
        } catch (e: Exception) {
            Log.e("TAG", "initialGetProfileData: ${e.message}")
            Toast.makeText(requireContext(), "Algo salio mal", Toast.LENGTH_SHORT).show()
            setupAlert(Constants.STATUS_ERROR) {}
        }
    }

    private fun initialPrintInfoUI(user: UserModel) {
        binding.txtFullName.text = user.userName
        binding.txtEmailUser.text = user.userEmail
        binding.txtEmailVeriofy.text = user.userVerified.toString()
        binding.txtPhoneUser.text = user.userPhone
        binding.txtAddressUser.text = user.userAddress
        binding.txtDateBirthUser.text = user.userDateBirth
        binding.txtBloodType.text = user.userBloodType
    }

    private fun initialGetBabiesData(idUser: String) {
        try {
            val babiesManager = BabiesManager()
            babiesManager.getMyBaby(idUser) {
                if (_dialog != null) {
                    _dialog?.dismissAlert()
                }
                if (it.isNotEmpty()) {
                    binding.rcvListSons.visibility = View.VISIBLE
                    binding.viewEmpty.visibility = View.GONE
                    initialRcvBabies(it)
                }
            }
        } catch (e: Exception) {
            setupAlert(Constants.STATUS_ERROR) {}
            Toast.makeText(requireContext(), "Algo salio mal", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initialRcvBabies(babies: List<BabyModel>) {
        val rcv = binding.rcvListSons
        rcv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rcv.setHasFixedSize(true)
        val adapter = BabyAdapter(babies)
        rcv.adapter = adapter
    }

    private fun actionButtons() {
        binding.btnRegisterBaby.setOnClickListener {
            val intent = Intent(requireContext(), RegisterBabyActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupAlert(case: Int, action: () -> Unit) {
        if (_dialog == null) {
            _dialog = DialogUi(requireContext())
        }

        when (case) {
            Constants.STATUS_LOADING -> {
                _dialog?.update(
                    R.raw.anim_loading,
                    R.string.loading_hint,
                    R.string.message_process_information,
                    actionCode = 1
                )
            }

            Constants.STATUS_SUCCESS -> {
                _dialog?.update(
                    R.raw.anim_success,
                    R.string.success_hint,
                    R.string.message_success,
                    actionCode = 1
                )
            }

            Constants.STATUS_ERROR -> {
                _dialog?.update(
                    R.raw.anim_error,
                    R.string.error_hint,
                    R.string.message_error,
                    actionCode = 1
                )
            }
        }

        _dialog?.show(action)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}