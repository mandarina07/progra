package com.example.proyecto

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.singleWindowApplication
import com.example.proyecto.interfaz.PantallaBoletas
import com.example.proyecto.interfaz.PantallaClientes
import com.example.proyecto.interfaz.PantallaLecturas
import com.example.proyecto.interfaz.PantallaMedidores
import com.example.proyecto.persistencia.*
import com.example.proyecto.servicios.BoletaService
import com.example.proyecto.servicios.TarifaService
import kotlinx.coroutines.launch

/**
 * Pantalla
 *
 * @constructor Create empty Pantalla
 */// Pantallas disponibles en la aplicación
enum class Pantalla {
    /**
     * Clientes
     *
     * @constructor Create empty Clientes
     */
    CLIENTES,

    /**
     * Medidores
     *
     * @constructor Create empty Medidores
     */
    MEDIDORES,

    /**
     * Lecturas
     *
     * @constructor Create empty Lecturas
     */
    LECTURAS,

    /**
     * Boletas
     *
     * @constructor Create empty Boletas
     */
    BOLETAS
}

/**
 * Main
 *
 */// Punto de entrada de la aplicación Desktop
fun main() = singleWindowApplication(
    title = "CGE Desktop"
) {
    AppDesktop()
}

/**
 * App desktop
 *
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDesktop() {
    // Estado del tema (claro/oscuro)
    val systemDark = isSystemInDarkTheme()
    var darkMode by remember { mutableStateOf(systemDark) }

    AppTheme(darkTheme = darkMode) {

        // Persistencia de datos compartida
        val persistenciaDatos = remember { PersistenciaDatos(FileStorageDriver()) }

        // Repositorios
        val clienteRepo = remember { ClienteRepositorioImpl(persistenciaDatos) }
        val boletaRepo = remember { BoletaRepositorioImpl(persistenciaDatos) }
        val lecturaRepo = remember { LecturaRepositorioImpl(persistenciaDatos) }
        val medidorRepo = remember { MedidorRepositorioImpl(persistenciaDatos) }

        // Servicios
        val tarifaService = remember { TarifaService() }
        val boletaService = remember {
            BoletaService(
                clienteRepositorio = clienteRepo,
                medidorRepositorio = medidorRepo,
                lecturaRepositorio = lecturaRepo,
                boletaRepositorio = boletaRepo,
                tarifaService = tarifaService
            )
        }

        // Estado de navegación
        var pantallaSeleccionada by remember { mutableStateOf(Pantalla.CLIENTES) }

        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()

        // Contenedor con menu lateral
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    Text(
                        "CGE Menú",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(16.dp)
                    )

                    NavigationDrawerItem(
                        label = { Text("Clientes") },
                        selected = pantallaSeleccionada == Pantalla.CLIENTES,
                        icon = { Icon(Icons.Filled.Person, contentDescription = null) },
                        onClick = {
                            pantallaSeleccionada = Pantalla.CLIENTES
                            scope.launch { drawerState.close() }
                        }
                    )

                    NavigationDrawerItem(
                        label = { Text("Medidores") },
                        selected = pantallaSeleccionada == Pantalla.MEDIDORES,
                        icon = { Icon(Icons.Filled.Settings, contentDescription = null) },
                        onClick = {
                            pantallaSeleccionada = Pantalla.MEDIDORES
                            scope.launch { drawerState.close() }
                        }
                    )

                    NavigationDrawerItem(
                        label = { Text("Lecturas") },
                        selected = pantallaSeleccionada == Pantalla.LECTURAS,
                        icon = { Icon(Icons.Filled.Bolt, contentDescription = null) },
                        onClick = {
                            pantallaSeleccionada = Pantalla.LECTURAS
                            scope.launch { drawerState.close() }
                        }
                    )

                    NavigationDrawerItem(
                        label = { Text("Boletas") },
                        selected = pantallaSeleccionada == Pantalla.BOLETAS,
                        icon = { Icon(Icons.Filled.ReceiptLong, contentDescription = null) },
                        onClick = {
                            pantallaSeleccionada = Pantalla.BOLETAS
                            scope.launch { drawerState.close() }
                        }
                    )
                }
            }
        ) {
            // Barra superior y contenido principal
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                when (pantallaSeleccionada) {
                                    Pantalla.CLIENTES -> "Clientes"
                                    Pantalla.MEDIDORES -> "Medidores"
                                    Pantalla.LECTURAS -> "Lecturas de consumo"
                                    Pantalla.BOLETAS -> "Boletas"
                                }
                            )
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        if (drawerState.isClosed) drawerState.open()
                                        else drawerState.close()
                                    }
                                }
                            ) {
                                Icon(Icons.Filled.Menu, contentDescription = "Menú")
                            }
                        },
                        actions = {
                            IconButton(onClick = { darkMode = !darkMode }) {
                                Icon(
                                    imageVector = if (darkMode) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                                    contentDescription = "Cambiar tema"
                                )
                            }
                        }
                    )
                }
            ) { paddingValues ->
                Surface(
                    modifier = Modifier.padding(paddingValues),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Contenido segun pantalla seleccionada
                    when (pantallaSeleccionada) {
                        Pantalla.CLIENTES -> PantallaClientes(clienteRepo = clienteRepo)

                        Pantalla.MEDIDORES -> PantallaMedidores(
                            medidorRepo = medidorRepo
                        )

                        Pantalla.LECTURAS -> PantallaLecturas(
                            medidorRepo = medidorRepo,
                            lecturaRepo = lecturaRepo
                        )

                        Pantalla.BOLETAS -> PantallaBoletas(
                            clienteRepo = clienteRepo,
                            boletaRepo = boletaRepo,
                            boletaService = boletaService
                        )
                    }
                }
            }
        }
    }
}
