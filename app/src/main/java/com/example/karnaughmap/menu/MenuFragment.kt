package com.example.karnaughmap.menu

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import com.example.karnaughmap.R
import com.example.karnaughmap.databinding.MenuFragmentBinding

class MenuFragment : Fragment() {

    private lateinit var menuViewModel: MenuViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<MenuFragmentBinding>(inflater, R.layout.menu_fragment, container, false)
        val args: MenuFragmentArgs by navArgs()
        val application = requireNotNull(this.activity).application

        val factory = MenuViewModelFactory(application, args.userId)
        menuViewModel = ViewModelProvider(this, factory).get(MenuViewModel::class.java)

        binding.lifecycleOwner = this
        binding.menuViewModel = menuViewModel

        menuViewModel.eventCreateNewMap.observe(viewLifecycleOwner, Observer {
            if (it) {
                this.findNavController().navigate(MenuFragmentDirections.actionMenuFragmentToKmapMainFragment(args.userId))
                menuViewModel.createNewMapComplete()
            }
        })

        // RecyclerView
        val adapter = MenuAdapter()
        binding.menuListRecyclerView.adapter = adapter

        menuViewModel.expressions.observe(viewLifecycleOwner, Observer {
            it?.let {
                Log.i("MenuFragment", it.toString())
                adapter.data = it
            }
        })

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.overflow_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController()) || super.onOptionsItemSelected(item)
    }
}