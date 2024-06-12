package com.l0122075.humamalwi.tubes.ui_user.dashboard

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.l0122075.humamalwi.tubes.DataFilm
import com.l0122075.humamalwi.tubes.MainAdapterUser
import com.l0122075.humamalwi.tubes.MainAdapterUser.Companion.VIEW_TYPE_NEWEST
import com.l0122075.humamalwi.tubes.MainAdapterUser.Companion.VIEW_TYPE_USER
import com.l0122075.humamalwi.tubes.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private lateinit var autoSlideAdapter: MainAdapterUser
    private lateinit var homeItemsAdapter: MainAdapterUser
    private lateinit var firebaseRef: DatabaseReference
    private lateinit var filmList: ArrayList<DataFilm>

    private val handler = Handler(Looper.getMainLooper())
    private var scrollPosition = 0

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        firebaseRef = FirebaseDatabase.getInstance().getReference("film")
        filmList = arrayListOf()

        binding.autoSlide.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        }
        binding.homeItems.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this.context, 3)
        }

        loadDataFromFirebase()

        return root
    }

    private fun loadDataFromFirebase() {
        firebaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                filmList.clear()
                if (snapshot.exists()) {
                    for (Snap in snapshot.children) {
                        val list = Snap.getValue(DataFilm::class.java)
                        filmList.add(list!!)
                    }
                    autoSlideAdapter = MainAdapterUser(filmList, false, true, VIEW_TYPE_NEWEST)
                    homeItemsAdapter = MainAdapterUser(filmList, false, false, VIEW_TYPE_USER)

                    binding.autoSlide.adapter = autoSlideAdapter
                    binding.homeItems.adapter = homeItemsAdapter


                    startAutoScroll()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun startAutoScroll() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (autoSlideAdapter.itemCount > 0) {
                    if (scrollPosition == autoSlideAdapter.itemCount) {
                        scrollPosition = 0
                    }
                    binding.autoSlide.smoothScrollToPosition(scrollPosition++)
                    handler.postDelayed(this, 3000) // Scroll every 2 seconds
                }
            }
        }, 3000)
    }


}
