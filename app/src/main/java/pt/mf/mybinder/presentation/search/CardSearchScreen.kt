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
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType.Companion.PrimaryNotEditable
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.focus.onFocusChanged
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
import pt.mf.mybinder.utils.Dimensions.FilterWindowApplyCloseTopPadding
import pt.mf.mybinder.utils.Dimensions.FilterWindowCategorySeparatorHeight
import pt.mf.mybinder.utils.Dimensions.FilterWindowCategorySeparatorVerticalPadding
import pt.mf.mybinder.utils.Dimensions.FilterWindowCategoryTitleEndPadding
import pt.mf.mybinder.utils.Dimensions.FilterWindowDropdownCornerRadius
import pt.mf.mybinder.utils.Dimensions.FilterWindowElevation
import pt.mf.mybinder.utils.Dimensions.FilterWindowInnerPadding
import pt.mf.mybinder.utils.Dimensions.FilterWindowOutterPadding
import pt.mf.mybinder.utils.Dimensions.FilterWindowRadioTitleEndPadding
import pt.mf.mybinder.utils.Dimensions.FilterWindowRadioTitleStartPadding
import pt.mf.mybinder.utils.Dimensions.FilterWindowSubCategoryRowTopPadding
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
                NothingToDisplayYet(viewState)
                NoResultsFound(viewState)
                ResultList(listState, viewState, viewModel)
                FilterWindow(viewState, viewModel)
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
                    Utils.tactileFeedback()
                    keyboardController?.hide()
                    focusManager.clearFocus()

                    if (!viewModel.isFilterReady()) {
                        viewModel.setFilterWindowVisibility(isVisibile = false)
                        Utils.toast(Utils.getString(R.string.card_search_filter_filter_not_ready))
                        return@IconButton
                    }

                    if (!viewState.isFilterWindowVisible)
                        viewModel.recallSelectedFilters()

                    viewModel.setFilterWindowVisibility(!viewState.isFilterWindowVisible)
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
                viewModel.changeIsNothingToDisplayVisibility(false)
                viewModel.changeNoResultsFoundVisibility(false)
                viewModel.fetchCards(query.trim())
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusChanged {
                viewModel.setFilterWindowVisibility(isVisibile = false)
            }
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
                viewModel.setFilterWindowVisibility(isVisibile = false)
                viewModel.setSelectedCardId(card.id)
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
                Text(text = viewModel.formatPrice(card.cardMarket?.prices?.lowPrice))
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
fun FilterWindow(viewState: CardSearchViewState, viewModel: CardSearchViewModel) {
    if (!viewState.isFilterWindowVisible)
        return

    ElevatedCard(
        elevation = CardDefaults.elevatedCardElevation(FilterWindowElevation),
        modifier = Modifier
            .fillMaxWidth()
            .padding(FilterWindowOutterPadding)
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
                        .padding(end = FilterWindowCategoryTitleEndPadding)
                        .weight(1f)
                )

                Column(
                    modifier = Modifier.weight(4f)
                ) {
                    FilterDropdown(
                        viewState.subtypes.map { it.name },
                        viewModel::getSelectedSubtype,
                        viewModel::setSelectedSubtype,
                        viewModel::setSubtypeFilterEnabled
                    )

                    Row(
                        modifier = Modifier.padding(top = FilterWindowSubCategoryRowTopPadding)
                    ) {
                        FilterRadioGroup(
                            listOf(
                                Utils.getString(R.string.card_search_filter_all),
                                Utils.getString(R.string.card_search_filter_specific)
                            ),
                            viewModel::isSubtypeFilterEnabled,
                            viewModel::setSubtypeFilterEnabled,
                            modifier = Modifier.weight(4f)
                        )
                    }
                }
            }

            FilterCategorySeparator(
                modifier = Modifier.padding(vertical = FilterWindowCategorySeparatorVerticalPadding)
            )

            Row {
                Text(
                    text = "${Utils.getString(R.string.card_search_filter_set)}:",
                    modifier = Modifier
                        .padding(end = FilterWindowCategoryTitleEndPadding)
                        .weight(1f)
                )

                Column(
                    modifier = Modifier.weight(4f)
                ) {
                    FilterDropdown(
                        viewState.sets.map { it.name },
                        viewModel::getSelectedSet,
                        viewModel::setSelectedSet,
                        viewModel::setSetFilterEnabled
                    )

                    Row(
                        modifier = Modifier.padding(top = FilterWindowSubCategoryRowTopPadding)
                    ) {
                        FilterRadioGroup(
                            listOf(
                                Utils.getString(R.string.card_search_filter_all),
                                Utils.getString(R.string.card_search_filter_specific)
                            ),
                            viewModel::isSetFilterEnabled,
                            viewModel::setSetFilterEnabled,
                            modifier = Modifier.weight(4f)
                        )
                    }
                }
            }

            FilterCategorySeparator(
                modifier = Modifier.padding(vertical = FilterWindowCategorySeparatorVerticalPadding)
            )

            Row {
                Text(
                    text = "${Utils.getString(R.string.card_search_filter_order)}:",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = FilterWindowCategoryTitleEndPadding)
                        .weight(1f)
                )

                FilterRadioGroup(
                    listOf(
                        Utils.getString(R.string.card_search_filter_order_alpha),
                        Utils.getString(R.string.card_search_filter_order_chrono)
                    ),
                    viewModel::getSelectedOrder,
                    viewModel::setSelectedOrder,
                    modifier = Modifier.weight(4f)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = FilterWindowApplyCloseTopPadding)
            ) {
                Text(
                    text = Utils.getString(R.string.general_close),
                    color = Theme.getPrimary(),
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            viewModel.setFilterWindowVisibility(isVisibile = false)
                            Utils.tactileFeedback()
                        }
                )

                Text(
                    text = Utils.getString(R.string.general_apply),
                    color = Theme.getPrimary(),
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            viewModel.setFilterWindowVisibility(isVisibile = false)
                            Utils.tactileFeedback()
                            viewModel.applyFilters()
                        }
                )
            }
        }
    }
}

@Composable
fun FilterCategorySeparator(modifier: Modifier) {
    Box(
        modifier = modifier
            .background(Theme.getPrimary())
            .fillMaxWidth()
            .height(FilterWindowCategorySeparatorHeight)
    ) {}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDropdown(
    options: List<String>,
    getSelectedOption: () -> String,
    setSelectedOption: (String) -> Unit,
    setRadioToSpecific: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = {
            isExpanded = it
            Utils.tactileFeedback()
        },
        modifier = modifier
    ) {
        TextField(
            value = getSelectedOption.invoke(),
            onValueChange = {},
            readOnly = true,
            shape = RoundedCornerShape(FilterWindowDropdownCornerRadius),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
            modifier = Modifier.menuAnchor(PrimaryNotEditable, true),
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option) },
                    onClick = {
                        setSelectedOption.invoke(option)
                        setRadioToSpecific(1)
                        isExpanded = false
                        Utils.tactileFeedback()
                    }
                )
            }
        }
    }
}

@Composable
fun FilterRadioGroup(
    options: List<String>,
    getSelectedOption: () -> Int,
    setSelectedOption: (Int) -> Unit,
    modifier: Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        options.forEach { option ->
            Row(
                modifier = Modifier.clickable(onClick = {
                    Utils.tactileFeedback()
                    setSelectedOption.invoke(options.indexOf(option))
                })
            ) {
                RadioButton(
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Theme.getPrimary(),
                        disabledSelectedColor = Theme.getPrimary()
                    ),
                    selected = (options.indexOf(option) == getSelectedOption.invoke()),
                    onClick = null,
                    enabled = false,
                )

                Text(
                    text = option,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(
                            start = FilterWindowRadioTitleStartPadding,
                            end = FilterWindowRadioTitleEndPadding
                        )
                )
            }
        }
    }
}

@Composable
fun NothingToDisplayYet(viewState: CardSearchViewState) {
    if (!viewState.isNothingToDisplay)
        return

    Text(
        text = Utils.getString(R.string.card_search_filter_order_nothing_yet),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize()
    )
}

@Composable
fun NoResultsFound(viewState: CardSearchViewState) {
    if (!viewState.isNoResultsFound)
        return

    Text(
        text = Utils.getString(R.string.card_search_filter_order_nothing_no_results),
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize()
    )
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