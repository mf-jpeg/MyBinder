package pt.mf.mybinder.presentation.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pt.mf.mybinder.R
import pt.mf.mybinder.domain.model.remote.Card
import pt.mf.mybinder.presentation.search.CardSearchViewModel.CardSearchViewState
import pt.mf.mybinder.presentation.search.HOLDER.TAG
import pt.mf.mybinder.presentation.theme.LoadingBackground
import pt.mf.mybinder.presentation.theme.Theme
import pt.mf.mybinder.utils.Dimensions.FilterWindowApplyTopPadding
import pt.mf.mybinder.utils.Dimensions.FilterWindowCategoryEndPadding
import pt.mf.mybinder.utils.Dimensions.FilterWindowInnerPadding
import pt.mf.mybinder.utils.Dimensions.FilterWindowTitleBottomPadding
import pt.mf.mybinder.utils.Dimensions.ListFabPadding
import pt.mf.mybinder.utils.Dimensions.ListHorizontalPadding
import pt.mf.mybinder.utils.Dimensions.ListItemCornerRadius
import pt.mf.mybinder.utils.Dimensions.ListItemDetailsBottomPadding
import pt.mf.mybinder.utils.Dimensions.ListItemElevation
import pt.mf.mybinder.utils.Dimensions.ListItemInfoLeftPadding
import pt.mf.mybinder.utils.Dimensions.ListItemInnerPadding
import pt.mf.mybinder.utils.Dimensions.ListItemOuterBottomPadding
import pt.mf.mybinder.utils.Dimensions.ListTopPadding
import pt.mf.mybinder.utils.Logger
import pt.mf.mybinder.utils.Utils
import pt.mf.mybinder.utils.Utils.empty

/**
 * Created by Martim Ferreira on 07/02/2025
 */
object HOLDER {
    const val TAG = "CardSearchScreen"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardSearchScreen(padding: PaddingValues) {
    val viewModel: CardSearchViewModel = viewModel()

    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        Column {
            SearchBar(viewState, viewModel)
            Box {
                ResultList(listState, viewState, viewModel)
                FilterWindow(viewState.isFilterWindowVisible)
            }
        }
        ListItemDetails(viewState, viewModel, bottomSheetState)
        ListFloatingActionButton(listState, coroutineScope)
        LoadingOverlay(viewState.isLoading)
    }
}

@Composable
fun SearchBar(viewState: CardSearchViewState, viewModel: CardSearchViewModel) {
    var query by remember { mutableStateOf(String.empty()) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    TextField(
        value = query,
        onValueChange = { query = it },
        placeholder = {
            Text(Utils.getString(R.string.card_search_placeholder))
        },
        leadingIcon = {
            IconButton(
                onClick = {
                    viewModel.modifyFilterWindowVisibility(!viewState.isFilterWindowVisible)
                }
            ) {
                Icon(imageVector = Icons.Default.FilterList, contentDescription = null)
            }
        },
        trailingIcon = {
            if (query.isEmpty())
                return@TextField

            IconButton(
                onClick = {
                    query = String.empty()
                    focusRequester.requestFocus()
                    keyboardController?.show()
                }
            ) {
                Icon(imageVector = Icons.Default.Clear, contentDescription = null)
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search,
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                viewModel.clearCardList()
                keyboardController?.hide()
                focusManager.clearFocus()
                viewModel.searchCard(query.trim())
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
    )
}

@Composable
fun ResultList(
    listState: LazyListState,
    viewState: CardSearchViewState,
    viewModel: CardSearchViewModel
) {
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = ListTopPadding)
            .padding(horizontal = ListHorizontalPadding)
    ) {
        items(viewState.cards) { card ->
            ListItem(card, viewModel)
        }
    }
}

@Composable
fun ListItem(card: Card, viewModel: CardSearchViewModel) {
    var imageLoaded by remember { mutableStateOf(false) }

    ElevatedCard(
        shape = RoundedCornerShape(ListItemCornerRadius),
        elevation = CardDefaults.elevatedCardElevation(ListItemElevation),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = ListItemOuterBottomPadding)
            .clickable {
                Logger.debug(TAG, "Tapped card with id \"${card.id}\"")
                viewModel.modifySelectedCardId(card.id)
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ListItemInnerPadding)
        ) {
            if (!imageLoaded) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .wrapContentSize(align = Alignment.Center)
                        .align(Alignment.CenterVertically)
                )
            }

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(card.images.small)
                    .crossfade(750)
                    .build(),
                contentDescription = null,
                onSuccess = { imageLoaded = true },
                onError = { imageLoaded = false }
            )

            Column(
                modifier = Modifier.padding(start = ListItemInfoLeftPadding)
            ) {
                Text(text = card.name)
                Text(text = card.superType)
                Text(text = card.set.name)
                Text(text = Utils.formatPrice(card.cardMarket?.prices?.lowPrice))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListItemDetails(
    viewState: CardSearchViewState,
    viewModel: CardSearchViewModel,
    bottomSheetState: SheetState
) {
    val card = viewState.cards.find { it.id == viewState.selectedCardId }
    var imageLoaded by remember { mutableStateOf(false) }

    if (card == null)
        return

    val id = "\"${card.id}\""
    Logger.debug(TAG, "Opened sheet for card with id $id.")

    ModalBottomSheet(
        sheetState = bottomSheetState,
        onDismissRequest = {
            Logger.debug(TAG, "Closed sheet for card with id $id.")
            viewModel.clearClickedCardId()
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center)
                .padding(bottom = ListItemDetailsBottomPadding)
        ) {
            if (!imageLoaded)
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentSize(align = Alignment.Center)
                )

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(card.images.large)
                    .crossfade(750)
                    .build(),
                contentDescription = null,
                onSuccess = { imageLoaded = true },
                onError = { imageLoaded = false }
            )
        }
    }
}

@Composable
fun ListFloatingActionButton(listState: LazyListState, coroutineScope: CoroutineScope) {
    val showButton by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 }
    }

    if (!showButton)
        return

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = ListFabPadding, end = ListFabPadding)
    ) {
        FloatingActionButton(
            onClick = {
                coroutineScope.launch {
                    listState.animateScrollToItem(0)
                }
            },
            containerColor = Theme.getPrimary(),
            contentColor = Color.White,
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Icon(Icons.Default.KeyboardArrowUp, contentDescription = null)
        }
    }
}

@Composable
fun FilterWindow(isWindowVisible: Boolean) {
    if (!isWindowVisible)
        return

    ElevatedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(FilterWindowInnerPadding)
        ) {
            Text(
                text = Utils.getString(R.string.card_search_filter_title),
                color = Theme.getPrimary(),
                modifier = Modifier.padding(bottom = FilterWindowTitleBottomPadding)
            )

            Row {
                Text(
                    text = "${Utils.getString(R.string.card_search_filter_subtype)}:",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = FilterWindowCategoryEndPadding)
                )
                FilterSubtypeList()
            }

            Text(
                text = Utils.getString(R.string.general_apply),
                color = Theme.getPrimary(),
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = FilterWindowApplyTopPadding)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterSubtypeList() {
//    var isExpanded by remember { mutableStateOf(false) }
//    var selectedOption by remember { mutableStateOf(options.first()) }
//
//    ExposedDropdownMenuBox(
//        expanded = isExpanded,
//        onExpandedChange = { isExpanded = it }
//    ) {
//        OutlinedTextField(
//            value = selectedOption,
//            onValueChange = {},
//            readOnly = true,
//            modifier = Modifier.menuAnchor(PrimaryNotEditable, true),
//        )
//
//        ExposedDropdownMenu(
//            expanded = isExpanded,
//            onDismissRequest = { isExpanded = false }
//        ) {
//            options.forEach { option ->
//                DropdownMenuItem(
//                    text = { Text(text = option) },
//                    onClick = {
//                        selectedOption = option
//                        isExpanded = false
//                    }
//                )
//            }
//        }
//    }
}

@Composable
fun LoadingOverlay(isLoading: Boolean) {
    if (!isLoading)
        return

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LoadingBackground)
            .pointerInput(Unit) { detectTapGestures { } }
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(align = Alignment.Center)
        )
    }
}