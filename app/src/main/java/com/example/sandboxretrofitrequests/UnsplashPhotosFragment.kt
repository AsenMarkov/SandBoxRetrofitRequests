package com.example.sandboxretrofitrequests

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sandboxretrofitrequests.databinding.FragmentUnsplashPhotosBinding
import kotlinx.android.synthetic.main.fragment_unsplash_photos.*


@Suppress("DEPRECATION")
class UnsplashPhotosFragment : Fragment(), PhotoAdapter.OnPhotoClickedListener, Transfer {

    private var mainMenu: Menu? = null
    private lateinit var binding: FragmentUnsplashPhotosBinding
    private lateinit var photoAdapter: PhotoAdapter
    lateinit var currentLayoutManager: LinearLayoutManager

    private lateinit var photoDataList: ArrayList<PhotoData>
    private var currentPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        binding = FragmentUnsplashPhotosBinding.inflate(layoutInflater)
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        mainMenu = menu
        inflater.inflate(R.menu.top_nav_bar, menu)
        showDeleteMenu(false)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_selected -> {
                deleteSelected()
                true
            }
            R.id.open_selected -> {
                transferSelected()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadPageList()
        photoAdapter = PhotoAdapter(this) { show -> showDeleteMenu(show) }

        binding.recyclerView.adapter = photoAdapter

        binding.recyclerView.apply {
            currentLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            this.layoutManager = currentLayoutManager
            this.adapter = photoAdapter
            setHasFixedSize(true)
        }

        photoDataList = ArrayList()
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val totalItemCount = photoAdapter.itemCount
                val lastVisibleItemPosition: Int =
                    currentLayoutManager.findLastVisibleItemPosition()

                if (dy > 0) {
                    if (lastVisibleItemPosition + 1 == totalItemCount) {
                        loadPageList()
                    }
                }
            }
        })
    }

    private fun loadPageList() {

    }

    private fun showDeleteMenu(show: Boolean) {
        mainMenu?.findItem(R.id.delete_selected)?.isVisible = show
        mainMenu?.findItem(R.id.open_selected)?.isVisible = show
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun deleteSelected() {
        AlertDialog.Builder(context).setTitle("Delete")
            .setMessage("Do you want to delete the items?")
            .setPositiveButton("Delete") { _, _ ->
                photoAdapter.deleteSelected()
                photoAdapter.notifyDataSetChanged()
                showDeleteMenu(false)
            }.setNegativeButton("Cancel") { _, _ -> }.show()
    }

    override fun transferSelected() {
        photoAdapter.getClickedPhoto().toTypedArray().let { photos ->
            val action =
                UnsplashPhotosFragmentDirections.actionUnsplashPhotosFragmentToSelectedPhotosFragment(
                    photos
                )
            findNavController().navigate(action)
        }
    }

    override fun onClickedPhoto() {
        photoAdapter.getClickedPhoto().toTypedArray().let { photos ->
            val action =
                UnsplashPhotosFragmentDirections.actionUnsplashPhotosFragmentToSelectedPhotosFragment(
                    photos
                )
            findNavController().navigate(action)
        }
    }

    override fun editorModeOn(enabled: Boolean) {
        showDeleteMenu(enabled)
    }
}

interface Transfer {
    fun transferSelected()
    fun deleteSelected()
}
