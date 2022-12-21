package com.example.sandboxretrofitrequests.ui.gallery

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sandboxretrofitrequests.BaseFragment
import com.example.sandboxretrofitrequests.R
import com.example.sandboxretrofitrequests.data.PhotoData
import com.example.sandboxretrofitrequests.databinding.FragmentUnsplashPhotosBinding
import com.example.sandboxretrofitrequests.domain.adapters.PhotoAdapter
import kotlinx.android.synthetic.main.fragment_unsplash_photos.*
import org.koin.androidx.viewmodel.ext.android.viewModel


@Suppress("DEPRECATION")
@SuppressLint("NotifyDataSetChanged")

class UnsplashPhotosFragment : BaseFragment(), PhotoAdapter.OnPhotoClickedListener, Transfer,
    BaseFragment.ActivityActions {

    private var rememberScrollPosition = 0

    private var mainMenu: Menu? = null
    private lateinit var binding: FragmentUnsplashPhotosBinding
    private lateinit var photoAdapter: PhotoAdapter
    lateinit var currentLayoutManager: LinearLayoutManager

    private val viewModel: UnsplashPhotosViewModel by viewModel()


    private lateinit var updatedPhotosList: ArrayList<PhotoData>
    private var searchFor: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        binding = FragmentUnsplashPhotosBinding.inflate(layoutInflater)
    }

    override fun onPause() {
        rememberScrollPosition = currentLayoutManager.findFirstCompletelyVisibleItemPosition()
        showHideBottomNavBar(false)
        super.onPause()
    }

    override fun onResume() {
        currentLayoutManager.scrollToPosition(rememberScrollPosition)
        photoAdapter.clearSelectedPhotos()
        showHideBottomNavBar(true)
        super.onResume()
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        mainMenu = menu
        inflater.inflate(R.menu.top_nav_bar, menu)
        showDeleteMenu(false)
        mainMenu?.findItem(R.id.logout)?.isVisible = true

        val search = mainMenu?.findItem(R.id.appSearchBar)
        val searchView = search?.actionView as SearchView
        searchView.queryHint = "Search"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                photoAdapter.clearAllPhotos()
                searchFor = query!!
                viewModel.getSearchedPhotos(searchFor)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                photoAdapter.clearAllPhotos()
                searchFor = newText!!
                viewModel.getSearchedPhotos(searchFor)
                return false
            }
        })

//        search.setOnActionExpandListener(object : OnActionExpandListener {
//            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
//                return false
//            }
//
//            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
//                viewModel.getInitialPhotos()
//                return true
//            }
//        })

        searchView.setOnCloseListener(object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                viewModel.getInitialPhotos()
                return false
            }
        })
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
            R.id.logout -> {
                logOut()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        photoAdapter = PhotoAdapter(this) { show -> showDeleteMenu(show) }

        binding.recyclerView.adapter = photoAdapter

        viewModel.getInitialPhotos()

        binding.recyclerView.apply {
            currentLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            this.layoutManager = currentLayoutManager
            this.adapter = photoAdapter
            setHasFixedSize(true)
        }

        lifecycleScope.launchWhenCreated {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                viewModel.stateFlow.collect {
                    when (it) {
                        is UnsplashPhotosViewModel.ScreenState.Error -> {
                            showProgressBar(false)
                        }
                        is UnsplashPhotosViewModel.ScreenState.Loading -> {
                            showProgressBar(true) // showing only on start
                        }
                        is UnsplashPhotosViewModel.ScreenState.Success -> {
                            showProgressBar(false)
                            val photos = it.photos
                            if (photos.isNotEmpty()) {
                                photos.let {
                                    val oldCount = updatedPhotosList.size
                                    updatedPhotosList.addAll(photos)
                                    photoAdapter.setNewPhoto(
                                        updatedPhotosList,
                                        oldCount,
                                        updatedPhotosList.size,
                                    )
                                }
                            } else if (it.photos.isEmpty()) {
                                Toast.makeText(context, "Response is empty", Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                }
            }
        }

        updatedPhotosList = ArrayList()

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val totalItemCount = photoAdapter.itemCount
                val lastVisibleItemPosition: Int =
                    currentLayoutManager.findLastVisibleItemPosition()

                if (dy > 0) {
                    if (lastVisibleItemPosition + 1 == totalItemCount) {
                        if (searchFor.isEmpty()) {
                            viewModel.getInitialPhotos()
                        } else {
                            viewModel.getSearchedPhotos(searchFor)
                        }
                    }
                }
            }
        })

        binding.container.setOnRefreshListener {
            photoAdapter.clearAllPhotos()
            viewModel.firstPage()
            if (searchFor.isEmpty()) {
                viewModel.getInitialPhotos()
            } else {
                viewModel.getSearchedPhotos(searchFor)
            }
            binding.container.isRefreshing = false
            showDeleteMenu(false)
        }
    }

    private fun showDeleteMenu(show: Boolean) {
        mainMenu?.findItem(R.id.delete_selected)?.isVisible = show
        mainMenu?.findItem(R.id.open_selected)?.isVisible = show
        mainMenu?.findItem(R.id.appSearchBar)?.isVisible = !show
    }

    override fun showProgressBar(show: Boolean) {
        actions?.showProgressBar(show)
    }

    override fun showHideBottomNavBar(show: Boolean) {
        actions?.showHideBottomNavBar(show)
    }

    override fun deleteSelected() {
        AlertDialog.Builder(context).setTitle("Delete")
            .setMessage("Do you want to delete the items?")
            .setPositiveButton("Delete") { _, _ ->
                photoAdapter.deleteSelected()
                photoAdapter.notifyDataSetChanged()
                showDeleteMenu(false)
            }.setNegativeButton("Cancel") { _, _ -> }.show()
    }

    private fun logOut() {
        val action =
            UnsplashPhotosFragmentDirections.actionUnsplashPhotosFragmentToLoginFragment(false)
        findNavController().navigate(action)
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
