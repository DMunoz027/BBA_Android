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
import com.doris.bba_android.databinding.FragmentProfileBinding
import com.doris.bba_android.model.BabyModel
import com.doris.bba_android.model.UserModel
import com.doris.bba_android.network.users.BabiesManager
import com.doris.bba_android.network.users.UserManager
import com.doris.bba_android.ui.home.RegisterBabyActivity
import com.doris.bba_android.ui.home.ui.adapters.BabyAdapter
import com.doris.bba_android.utils.SharedPreferencesManager

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

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
        initialGetBabiesData()

    }

    private fun initialGetProfileData() {
        try {
            val userManager = UserManager()
            val prefs = SharedPreferencesManager(requireContext())
            val idUser = prefs.getPref("idUser", "empty")
            userManager.getOneUserColl(idUser.toString()) {
                if (it != null) {
                    initialPrintInfoUI(it)
                }
            }
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Algo salio mal", Toast.LENGTH_SHORT).show()
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

    private fun initialGetBabiesData() {
        try {
            val babiesManager = BabiesManager()
            val prefs = SharedPreferencesManager(requireContext())
            val idUser = prefs.getPref("idUser", "empty")
            babiesManager.getMyBaby(idUser.toString()) {
                if (it.isNotEmpty()) {
                    binding.rcvListSons.visibility = View.VISIBLE
                    binding.viewEmpty.visibility = View.GONE
                    initialRcvBabies(it)
                }
            }
        } catch (e: Exception) {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}