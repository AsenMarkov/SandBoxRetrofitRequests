package com.example.sandboxretrofitrequests

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sandboxretrofitrequests.databinding.FragmentUnsplashPhotosBinding
import kotlinx.android.synthetic.main.fragment_unsplash_photos.*
import org.koin.androidx.viewmodel.ext.android.viewModel


@Suppress("DEPRECATION")
class UnsplashPhotosFragment : BaseFragment(), PhotoAdapter.OnPhotoClickedListener, Transfer,
    BaseFragment.ActivityActions {

    private var mainMenu: Menu? = null
    private lateinit var binding: FragmentUnsplashPhotosBinding
    private lateinit var photoAdapter: PhotoAdapter
    lateinit var currentLayoutManager: LinearLayoutManager

    private val viewModel: UnsplashPhotosViewModel by viewModel()


    private lateinit var updatedPhotosList: ArrayList<PhotoData>
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
        mainMenu?.findItem(R.id.logout)?.isVisible = true
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

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        photoAdapter = PhotoAdapter(this) { show -> showDeleteMenu(show) }

        binding.recyclerView.adapter = photoAdapter

        viewModel.fetchPhotos(currentPage)

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
                        }
                        is UnsplashPhotosViewModel.ScreenState.Loading -> {
                            showProgressBar(true)
                        }
                        is UnsplashPhotosViewModel.ScreenState.Success -> {
                            val photos = it.success
                            if (photos.isNotEmpty()) {
                                photos.let {
                                    val oldCount = updatedPhotosList.size
                                    updatedPhotosList.addAll(photos)
                                    photoAdapter.setNewPhoto(
                                        photos,
                                        updatedPhotosList,
                                        oldCount,
                                        updatedPhotosList.size,
                                    )
                                }
                            } else if (it.success.isEmpty()) {
                                Toast.makeText(context, "Response is empty", Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT)
                                    .show()
                            }
                            showProgressBar(false)
                            currentPage++
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
                        viewModel.fetchPhotos(currentPage)
                    }
                }
            }
        })
        binding.container.setOnRefreshListener {
            photoAdapter.clearAllPhotos()
            currentPage = 1
            viewModel.fetchPhotos(currentPage)
            binding.container.isRefreshing = false
        }
    }

    private fun showDeleteMenu(show: Boolean) {
        mainMenu?.findItem(R.id.delete_selected)?.isVisible = show
        mainMenu?.findItem(R.id.open_selected)?.isVisible = show
    }

    override fun showProgressBar(show: Boolean) {
        actions?.showProgressBar(show)
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
