package com.sworks.ui.home

import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.sworks.R
import com.sworks.api.Status
import com.sworks.databinding.FragmentHomeBinding
import com.sworks.model.ClientList
import com.sworks.model.Order
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment(), OnMapReadyCallback {

    lateinit var binding: FragmentHomeBinding

    private lateinit var googleMap: GoogleMap
    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val size = (Resources.getSystem().displayMetrics.heightPixels) / 2
        val behavior = BottomSheetBehavior.from(binding.temp.bottomSheet)
        behavior.peekHeight = size

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.progressBar.bringToFront()

        return binding.root
    }

    override fun onMapReady(map: GoogleMap) {

        googleMap = map
        lifecycleScope.launchWhenCreated {

            viewModel.getClinetList().observe(viewLifecycleOwner, {

                when(it.status) {
                    Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    Status.SUCCESS -> {
                        updateUI(it.data)
                        binding.progressBar.visibility = View.GONE

                    }
                    Status.HTTP_ERROR -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }

                    Status.API_ERROR -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                    }
                }
            })
        }
    }

    private fun updateUI(data: ClientList?) {
        data?.let {

            val clientAdapter = ClientAdapter { view ->

                val order = view.tag as Order
                when (view.id) {
                    R.id.callButton -> {
                        val intent = Intent(Intent.ACTION_CALL)
                        intent.data = Uri.parse("tel:${order.phone}")
                        startActivity(intent)
                    }

                    R.id.directionButton -> {
                        val gmmIntentUri =
                            Uri.parse("google.navigation:q=${order.location.lat},${order.location.long}")
                        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                        mapIntent.setPackage("com.google.android.apps.maps")
                        startActivity(mapIntent)
                    }
                }
            }

            binding.temp.clientListView.adapter = clientAdapter
            clientAdapter.submitList(it.orders)

            val builder = LatLngBounds.Builder()

            for (temp in it.orders) {
                builder.include(LatLng(temp.location.lat, temp.location.long))
                googleMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(temp.location.lat, temp.location.long))
                        .title(temp.name)
                )
            }
            val bounds = builder.build()
            val cu = CameraUpdateFactory.newLatLngBounds(bounds, 18)
            googleMap.moveCamera(cu)
        }
    }
}