package lab.mobile.frontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.navArgument
import lab.mobile.frontend.ui.book_add.AddBookScreen
import lab.mobile.frontend.ui.book_add.BookAddViewModel
import lab.mobile.frontend.ui.book_detail.BookDetailScreen
import lab.mobile.frontend.ui.book_detail.BookDetailViewModel
import lab.mobile.frontend.ui.book_list.BookListScreen
import lab.mobile.frontend.ui.book_list.BookListViewModel
import lab.mobile.frontend.ui.login.LoginScreen
import lab.mobile.frontend.ui.login.LoginViewModel
import lab.mobile.frontend.ui.profile.ProfileScreen
import lab.mobile.frontend.ui.theme.FrontendTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FrontendTheme() {
                BookAppContent()
            }
        }
    }
}

@Composable
fun BookAppContent() {
    val navController = rememberNavController()

    val app =
        (LocalContext.current.applicationContext as BookApplication)
    val container = app.container

    NavHost(navController = navController, startDestination = "login") {


        composable("login") {
            val viewModel: LoginViewModel = viewModel(
                factory = ViewModelFactoryHelper {
                    LoginViewModel(container.authRepository)
                }
            )

            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate("books") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("books") {
            val viewModel: BookListViewModel = viewModel(
                factory = ViewModelFactoryHelper {
                    BookListViewModel(container.bookRepository)
                }
            )

            BookListScreen(
                viewModel = viewModel,
                onBookClick = { bookId ->
                    navController.navigate("book_detail/$bookId")
                },
                onProfileClick = {
                    navController.navigate("profile")
                },
                onAddClick = {
                    // Replace "add_book" with whatever string you defined in your NavHost for the add screen
                    navController.navigate("add_book")
                }
            )
        }

        composable(
            route = "book_detail/{bookId}",
            arguments = listOf(navArgument("bookId") { type = NavType.StringType })
        ) { backStackEntry ->
            val bookIdString = backStackEntry.arguments?.getString("bookId")
            val bookId = bookIdString?.toIntOrNull() ?: -1

            val detailViewModel: BookDetailViewModel = viewModel(
                factory = ViewModelFactoryHelper {
                    BookDetailViewModel(container.bookRepository, bookId)
                }
            )

            BookDetailScreen(
                viewModel = detailViewModel,
                onBack = { navController.popBackStack() }
            )
        }

        composable("add_book") {

            val addViewModel: BookAddViewModel = viewModel(
                factory = ViewModelFactoryHelper {
                    BookAddViewModel(container.bookRepository)
                }
            )

            AddBookScreen(
                onBack = { navController.popBackStack() },
                onSave = { title, author, year ->
                    addViewModel.addBook(title, author, year)
                    navController.popBackStack()
                }
            )
        }
        composable("profile") {
            ProfileScreen()
        }
    }
}


class ViewModelFactoryHelper<T : ViewModel>(
    private val producer: () -> T
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return producer() as T
    }
}