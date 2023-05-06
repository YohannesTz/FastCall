package com.github.yohannestz.fastcall.ui

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.yohannestz.fastcall.R
import com.github.yohannestz.fastcall.data.model.ui.DialogUiState
import com.github.yohannestz.fastcall.data.model.ui.UiState
import com.github.yohannestz.fastcall.ui.adapters.ContactsAdapter
import com.github.yohannestz.fastcall.ui.fragments.InputDialogFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.permissionx.guolindev.PermissionX
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), InputDialogFragment.InputDialogFragmentListener {

    private val mainViewModel: MainViewModel by viewModels()


    @Inject
    lateinit var contactsAdapter: ContactsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_main)

        val contactsListView: RecyclerView = findViewById(R.id.contacts_listview)
        val progressBar: ProgressBar = findViewById(R.id.contactsProgressBar)
        val errorView: LinearLayout = findViewById(R.id.error_view)
        val emptyView: LinearLayout = findViewById(R.id.empty_view)
        val swipeLayout: SwipeRefreshLayout = findViewById(R.id.swipeRefresh)
        val addContactsFab: FloatingActionButton =
            findViewById(R.id.addContactsFloatingActionButton)

        contactsListView.layoutManager = GridLayoutManager(this, 2)
        contactsListView.adapter = contactsAdapter

        swipeLayout.setOnRefreshListener {
            mainViewModel.getAllContacts()
        }

        PermissionX.init(this)
            .permissions(android.Manifest.permission.CALL_PHONE)
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(deniedList, "Core fundamental are based on these permissions", "OK", "Cancel")
            }
            .request { allGranted, _, _ ->
                if (!allGranted) {
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage("This app requires permission to access your location. Would you like to grant permission?")
                        .setPositiveButton("Yes") { _, _ ->
                            // User clicked Yes button
                            // Request permission here
                        }
                    val dialog = builder.create()
                    dialog.show()
                }
            }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.contactsFlow.collect {
                    when (it) {
                        is UiState.Success -> {
                            progressBar.visibility = View.GONE
                            swipeLayout.isRefreshing = false
                            emptyView.visibility = View.GONE

                            if (it.data.isEmpty()) {
                                emptyView.visibility = View.VISIBLE
                            } else {
                                contactsAdapter.differ.submitList(it.data)
                                contactsListView.visibility = View.VISIBLE
                            }
                        }

                        is UiState.Loading -> {
                            progressBar.visibility = View.VISIBLE
                            contactsListView.visibility = View.GONE
                        }

                        is UiState.Error -> {
                            contactsListView.visibility = View.GONE
                            errorView.visibility = View.VISIBLE
                            emptyView.visibility = View.GONE
                        }
                    }
                }
            }

            repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainViewModel.dialogFlow.collect {
                    when (it) {
                        is DialogUiState.AddState -> {
                            val rootView = window.decorView.rootView
                            if (it.isSuccessful) {
                                val snackBar = Snackbar
                                    .make(
                                        rootView,
                                        "Successfully inserted contact!",
                                        Snackbar.LENGTH_LONG
                                    )
                                snackBar.show()
                            } else {
                                val snackBar = Snackbar
                                    .make(
                                        rootView,
                                        "Something was wrong!",
                                        Snackbar.LENGTH_LONG
                                    )
                                snackBar.show()
                            }
                        }

                        else -> {}
                    }
                }
            }
        }

        addContactsFab.setOnClickListener {
            val inputDialog = InputDialogFragment()
            inputDialog.show(supportFragmentManager, "input dialog")

        }
    }

    override fun applyTexts(name: String, phoneNum: String) {
        mainViewModel.addContacts(name, phoneNum)
    }
}