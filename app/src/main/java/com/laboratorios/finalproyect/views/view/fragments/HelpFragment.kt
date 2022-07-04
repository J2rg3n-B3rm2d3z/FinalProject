package com.laboratorios.finalproyect.views.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.laboratorios.finalproyect.R
import com.laboratorios.finalproyect.databinding.FragmentHelpBinding
import com.laboratorios.finalproyect.views.view.adapter.PageViewAdapter
import me.relex.circleindicator.CircleIndicator3

class HelpFragment : Fragment() {

    private var titleList = mutableListOf<String>()
    private var descriptionList = mutableListOf<String>()
    private var imageList = mutableListOf<Int>()

    private var _binding:FragmentHelpBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHelpBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postToList()

        val viewPager = view.findViewById<ViewPager2>(R.id.viewpager)
        viewPager.adapter = PageViewAdapter(titleList, descriptionList, imageList)
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        val indicator = view.findViewById<CircleIndicator3>(R.id.indicator)
        indicator.setViewPager(viewPager)
    }

    private fun addToLists(title:String, description:String, image:Int){
        titleList.add(title)
        descriptionList.add(description)
        imageList.add(image)
    }

    private fun postToList(){
        addToLists("Ver estado del cajero", "Haz click en el cajero para ver el estado", R.drawable.img1)
        addToLists("Ver información del cajero", "Visualiza el estado actual y hora de actualización", R.drawable.img5)
        addToLists("Actualiza estado del cajero", "Haz click en la ventana de infomación", R.drawable.img2)
        addToLists("Guarda cambios", "Presiona 'Sí' para actualizar el estado del cajero", R.drawable.img3)
        addToLists("Ver cambios", " Estado actualizado", R.drawable.img4)
    }
}