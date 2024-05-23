package com.example.dialfilebooth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel


@Composable
fun MainScreen(
    viewModel: MainViewModel,
    navigateToContactList: () -> Unit,
    navigateToSecureContactList: () -> Unit
) {
    var name by rememberSaveable { mutableStateOf("") }
    var contact by rememberSaveable { mutableStateOf("") }
    var designation by rememberSaveable { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        // Text fields for name and contact
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier= Modifier.height(8.dp))
        OutlinedTextField(
            value = contact,
            onValueChange = { contact = it },
            label = { Text("Contact") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = designation,
            onValueChange = { designation = it },
            label = { Text("Designation") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.align(Alignment.End),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = {
                viewModel.addContact(name, contact, designation )
                name = ""
                contact = ""
                designation = " "
            },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.White,
                    containerColor = Color.Green)
            ) {
                Text("Add Contact")
            }
            Button(onClick = navigateToContactList,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.White,
                    containerColor = Color.Green)) {
                Text("Show Contacts")
            }

        }
        Row {
            Button(
                onClick = { navigateToSecureContactList() },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.White,
                    containerColor = Color.Blue
                )

            ) {
                Text("Secure Contact")
            }
            BiometricButton(
                onClick = navigateToSecureContactList,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color.White,
                    containerColor = Color.Red
                )
            )

        }
    }
}
@Composable
fun BiometricButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.textButtonColors()
) {
    val biometricManager = androidx.biometric.BiometricManager.from(LocalContext.current)
    val canAuthenticate = biometricManager.canAuthenticate()

    if (canAuthenticate == androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS) {
        Button(
            onClick = onClick,
            modifier = modifier,
            colors = colors
        ) {
            Text("show Secure contacts")
        }
    } else {
        // Biometric authentication is not available, show a message to the user
        Text(text = "Biometric authentication is not available on this device.")
    }
}

@Composable
fun ContactListScreen(viewModel: MainViewModel) {
    LazyColumn {
        items(viewModel.contacts) { contact->
            Surface(
                shape = MaterialTheme.shapes.medium,tonalElevation = 4.dp,
                modifier = Modifier.padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically) {

                    Column(modifier = Modifier.weight(1f)) {
                        CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.headlineMedium) {
                            Text(text = contact.name)
                        }
                        CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.bodyMedium) {
                            Text(text = contact.contact)
                        }
                    }
                    // Designation
                    Box(
                        modifier = Modifier
                            .padding(8.dp)
                            .background(color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(4.dp))
                    ) {
                        CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.labelMedium) {
                            Text(
                                text = contact.designation,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun SecureContactListScreen(viewModel: MainViewModel) {
    // Display the list of secure contacts
    LazyColumn {
        items(viewModel.secureContacts) { contact ->
            Text(text = contact.name + ": " + contact.contact )
        }
    }
}

class MainViewModel : ViewModel() {
    private val _contacts = mutableStateListOf<Contact>()
    val contacts: List<Contact> = _contacts

    private val _secureContacts = mutableStateListOf<SecureContact>()
    val secureContacts: List<SecureContact> = _secureContacts


    fun addContact(name: String, contact: String, designation: String) {
        _contacts.add(Contact(name, contact, designation))
    }

    fun secureContact(contact: Contact) {

        val secureContact = SecureContact(contact.name, contact.contact)
        _secureContacts.add(secureContact)
    }
}

data class Contact(val name: String, val contact: String, val designation: String)
data class SecureContact(val name: String, val contact: String)