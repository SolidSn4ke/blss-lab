module Main exposing (Model, Msg(..), init, main, subscriptions, update, view)

import Booking exposing (Booking)
import Bootstrap.Alert as Alert
import Bootstrap.Button as Button
import Bootstrap.CDN as CDN
import Bootstrap.Card as Card
import Bootstrap.Card.Block as Block
import Bootstrap.Form as Form
import Bootstrap.Form.Input as Input
import Bootstrap.Grid as Grid exposing (Column)
import Bootstrap.Grid.Col as Col
import Bootstrap.Grid.Row as Row
import Bootstrap.Modal as Modal
import Bootstrap.Navbar as Navbar
import Bootstrap.Text as Text
import Browser
import Housing exposing (Housing, HousingType(..), listHousingDecoder, show)
import Html exposing (..)
import Html.Attributes exposing (class, for, href, style)
import Html.Events exposing (..)
import Http exposing (Error(..))
import List
import Response exposing (Response, responseDecoder)
import String exposing (fromInt)
import SvgResources exposing (svgHouse)
import Time



-- MAIN


main : Program () Model Msg
main =
    Browser.element
        { init = init
        , update = update
        , subscriptions = subscriptions
        , view = view
        }



-- MODEL


type alias Model =
    { navbarState : Navbar.State
    , page : Page
    , housings : List Housing
    , error : Maybe String
    , username : String
    , modalVisibility : Modal.Visibility
    , alertVisibility : Alert.Visibility
    , bookingInfo : Booking
    }


type Page
    = BookingPage
    | Profile
    | AddHousing


init : () -> ( Model, Cmd Msg )
init _ =
    let
        ( navbarState, navbarCmd ) =
            Navbar.initialState NavbarMsg
    in
    ( { navbarState = navbarState, page = BookingPage, housings = [], error = Nothing, username = "", modalVisibility = Modal.hidden, alertVisibility = Alert.closed, bookingInfo = Booking.empty }, navbarCmd )



-- UPDATE


type Msg
    = NavbarMsg Navbar.State
    | NavTo Page
    | ShowModal
    | CloseModal
    | AlertMsg Alert.Visibility
    | UpdateBooking String String
    | RequestBooking Int
    | ReceiveHousings (Result Error (List Housing))
    | ReceiveResponse (Result Error (Response Housing))
    | ChangeUsername String
    | Tick Time.Posix


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        NavbarMsg s ->
            ( { model | navbarState = s }, Cmd.none )

        NavTo p ->
            ( { model | page = p }, Cmd.none )

        ShowModal ->
            ( { model | modalVisibility = Modal.shown }, Cmd.none )

        CloseModal ->
            ( { model | modalVisibility = Modal.hidden, bookingInfo = Booking.empty }, Cmd.none )

        AlertMsg v ->
            ( { model | alertVisibility = v, modalVisibility = Modal.hidden }, Cmd.none )

        UpdateBooking field value ->
            ( { model | bookingInfo = Booking.updateBooking field value model.bookingInfo }, Cmd.none )

        RequestBooking id ->
            ( { model | modalVisibility = Modal.hidden }, requestBooking model.username id model.bookingInfo )

        ReceiveHousings result ->
            case result of
                Ok housings ->
                    ( { model | housings = housings, error = Nothing }, Cmd.none )

                Err error ->
                    case error of
                        Http.BadUrl url ->
                            ( { model | error = Just <| "Bad URL: " ++ url }, Cmd.none )

                        Timeout ->
                            ( { model | error = Just <| "Timeout occurred while fetching housings" }, Cmd.none )

                        NetworkError ->
                            ( { model | error = Just <| "Network error occurred while fetching housings" }, Cmd.none )

                        BadStatus _ ->
                            ( { model | error = Just <| "Bad status occurred while fetching housings" }, Cmd.none )

                        BadBody _ ->
                            ( { model | error = Just <| "Bad body occurred while fetching housings" }, Cmd.none )

        Tick _ ->
            if model.page == BookingPage then
                ( model, fetchHousings )

            else
                ( model, Cmd.none )

        ChangeUsername newUsername ->
            ( { model | username = newUsername }, Cmd.none )

        ReceiveResponse res ->
            case res of
                Ok _ ->
                    ( { model | alertVisibility = Alert.shown }, Cmd.none )

                Err _ ->
                    ( { model | alertVisibility = Alert.shown, error = Just "smth bad happend" }, Cmd.none )



-- SUBSCRIPTIONS


subscriptions : Model -> Sub Msg
subscriptions model =
    Sub.batch [ Navbar.subscriptions model.navbarState NavbarMsg, Time.every 5000 Tick ]



-- VIEW


view : Model -> Html Msg
view model =
    div []
        [ Navbar.config NavbarMsg
            |> Navbar.withAnimation
            |> Navbar.items
                [ Navbar.itemLink [ href "#BookingPage", onClick <| NavTo BookingPage ] [ text "Бронирование" ]
                , Navbar.itemLink [ href "#add-housing", onClick <| NavTo AddHousing ] [ text "Добавить жилье" ]
                , Navbar.itemLink [ href "#profile", onClick <| NavTo Profile ] [ text "Профиль" ]
                ]
            |> Navbar.view model.navbarState
        , div []
            [ Alert.config
                |> (case model.error of
                        Just _ ->
                            Alert.danger

                        Nothing ->
                            Alert.info
                   )
                |> Alert.dismissable AlertMsg
                |> Alert.children
                    [ case model.error of
                        Just str ->
                            text str

                        Nothing ->
                            text "Успешный запрос"
                    ]
                |> Alert.view model.alertVisibility
            ]
        , Grid.container []
            [ CDN.stylesheet
            , layoutChooser model
            ]
        ]


layoutChooser : Model -> Html Msg
layoutChooser model =
    case model.page of
        BookingPage ->
            div [ style "margin" "10" ]
                [ Grid.row [ Row.centerLg, Row.attrs <| [ style "padding" "10", style "margin" "10" ] ] <|
                    List.map (\h -> housingCard h model.modalVisibility model.bookingInfo) model.housings
                , text <|
                    case model.error of
                        Just err ->
                            "Error: " ++ err

                        Nothing ->
                            ""
                ]

        Profile ->
            div []
                [ Form.form []
                    [ Form.group []
                        [ Form.label [ for "username-input" ] [ text "Введите имя пользователя" ]
                        , Input.text [ Input.id "username-input", Input.onInput ChangeUsername ]
                        , Form.help [] [ text "Это имя будет использоваться при обращении к серверу." ]
                        ]
                    ]
                ]

        AddHousing ->
            text "AddHousing page is not yet implemented"


housingCard : Housing -> Modal.Visibility -> Booking -> Column Msg
housingCard h v b =
    Grid.col [ Col.lgAuto ]
        [ Card.config [ Card.align Text.alignXsCenter, Card.attrs [ class "mt-3" ] ]
            |> Card.header [] [ svgHouse ]
            |> Card.block [ Block.align Text.alignLgLeft ]
                [ Block.titleH3 [] [ text <| show h.housingType ]
                , Block.text [] [ text <| "ул. " ++ h.address.street ++ ", " ++ h.address.country ]
                , Block.text [] [ text <| fromInt h.price ++ " руб./ночь" ]
                , Block.custom <| Button.button [ Button.outlinePrimary, Button.onClick ShowModal ] [ text "Забронировать" ]
                ]
            |> Card.view
        , Modal.config CloseModal
            |> Modal.hideOnBackdropClick True
            |> Modal.large
            |> Modal.h3 [] [ text "Введите данные для бронирования" ]
            |> Modal.body []
                [ Form.form []
                    [ Form.row []
                        [ Form.colLabel [ Col.sm4 ] [ text "Даты проживания" ]
                        , Form.col [] [ Input.date [ Input.id "check-in-input", Input.onInput <| UpdateBooking "checkin", Input.value b.checkIn ] ]
                        , Form.col [] [ Input.date [ Input.id "check-out-input", Input.onInput <| UpdateBooking "checkout", Input.value b.checkOut ] ]
                        ]
                    , Form.row []
                        [ Form.colLabel [ Col.sm4 ] [ text "Кол-во взрослых" ]
                        , Form.col [] [ Input.number [ Input.onInput <| UpdateBooking "adultscount", Input.value <| fromInt b.adultsCount ] ]
                        ]
                    , Form.row []
                        [ Form.colLabel [ Col.sm4 ] [ text "Кол-во детей" ]
                        , Form.col [] [ Input.number [ Input.onInput <| UpdateBooking "childcount", Input.value <| fromInt b.childCount ] ]
                        ]
                    , Form.row []
                        [ Form.colLabel [ Col.sm4 ] [ text "Кол-во младенцев" ]
                        , Form.col [] [ Input.number [ Input.onInput <| UpdateBooking "infantscount", Input.value <| fromInt b.infantsCount ] ]
                        ]
                    , Form.row []
                        [ Form.colLabel [ Col.sm4 ] [ text "Кол-во животных" ]
                        , Form.col [] [ Input.number [ Input.onInput <| UpdateBooking "petscount", Input.value <| fromInt b.petsCount ] ]
                        ]
                    ]
                ]
            |> Modal.footer []
                [ Button.button [ Button.danger, Button.onClick CloseModal ] [ text "Отмена" ]
                , Button.button [ Button.primary, Button.onClick <| RequestBooking h.id ] [ text "Подтвердить" ]
                ]
            |> Modal.view v
        ]



-- HTTP


fetchHousings : Cmd Msg
fetchHousings =
    Http.get
        { url = "http://localhost:8080/housing/all"
        , expect = Http.expectJson ReceiveHousings listHousingDecoder
        }


requestBooking : String -> Int -> Booking -> Cmd Msg
requestBooking username id b =
    Http.post
        { url = "http://localhost:8080/booking/" ++ username ++ "/require-housing/" ++ fromInt id
        , body = Http.jsonBody <| Booking.encodeBooking b
        , expect = Http.expectJson ReceiveResponse responseDecoder
        }
