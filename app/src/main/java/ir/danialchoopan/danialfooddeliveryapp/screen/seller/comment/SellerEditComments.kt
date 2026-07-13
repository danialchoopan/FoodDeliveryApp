package ir.danialchoopan.danialfooddeliveryapp.screen.seller.comment

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import ir.danialchoopan.danialfooddeliveryapp.data.seller.SellerCommentRequestGroup
import ir.danialchoopan.danialfooddeliveryapp.models.seller.comment.Comment
import ir.danialchoopan.danialfooddeliveryapp.screen.functions.*
import ir.danialchoopan.danialfooddeliveryapp.ui.theme.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerCommentScreen(navHostController: NavHostController) {

    var isDeleteDialogOpen by remember { mutableStateOf(false) }
    var selected_item_id by remember { mutableStateOf(0) }


    var listComment by remember {
        mutableStateOf(emptyList<Comment>())
    }

    var onGoingProgress by remember {
        mutableStateOf(true)
    }

    val m_context = LocalContext.current

    val seller_comment_request_group = SellerCommentRequestGroup(m_context)

    seller_comment_request_group.showCommentSeller { _, it ->
        listComment = it.comments
        onGoingProgress = false
    }


    Scaffold(
        containerColor = BackgroundColor,
        topBar = {
            GradientTopBar(
                title = "لیست نظرات رستوران",
                actions = {
                    IconButton(onClick = {
                        navHostController.popBackStack()
                    }) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "برگشت",
                            modifier = Modifier.size(42.dp),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        if (onGoingProgress) {

            LoadingProgressbar {

            }

        } else {
            if (listComment.isEmpty()) {
                EmptyState(
                    message = "درمورد رستوران شما هنوز نظری ثبت نشده است",
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(16.dp)
                ) {
                    items(listComment) { comment ->
                        ModernCard(
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                ListItem(
                                    headlineContent = {
                                        Text(
                                            text = comment.commentContent,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = TextPrimary
                                        )
                                    },
                                    supportingContent = {},
                                    trailingContent = {},
                                    modifier = Modifier.padding(8.dp)
                                )

                                Row(
                                    horizontalArrangement = Arrangement.End,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    IconButton(onClick = {
                                        isDeleteDialogOpen = true
                                        selected_item_id = comment.commentId
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "حذف",
                                            tint = ErrorColor
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (isDeleteDialogOpen) {
                AlertDialog(
                    onDismissRequest = {
                        isDeleteDialogOpen = false
                    },
                    title = {
                        Text(text = "آیا مطمئن هستید؟")
                    },
                    text = {
                        Text(text = "این آیتم حذف خواهد شد. این عمل قابل بازگشت نیست.")
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            seller_comment_request_group.DeleteCommentSeller(selected_item_id.toString()) {

                                onGoingProgress = true
                                seller_comment_request_group.showCommentSeller { _, it ->
                                    listComment = it.comments
                                    onGoingProgress = false
                                }
                            }
                            isDeleteDialogOpen = false
                        }) {
                            Text(text = "بله", color = ErrorColor)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            isDeleteDialogOpen = false
                        }) {
                            Text(text = "خیر")
                        }
                    }
                )
            }

        }
    }
}
