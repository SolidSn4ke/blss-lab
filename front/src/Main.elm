module Main exposing (Model, Msg(..), init, main, subscriptions, update, view)

import Bootstrap.Button as Button
import Bootstrap.CDN as CDN
import Bootstrap.Card as Card
import Bootstrap.Card.Block as Block
import Bootstrap.Grid as Grid exposing (Column)
import Bootstrap.Grid.Col as Col
import Bootstrap.Grid.Row as Row
import Bootstrap.Navbar as Navbar
import Bootstrap.Text as Text
import Browser
import Housing exposing (Housing, HousingType(..), show)
import Html exposing (..)
import Html.Attributes exposing (href, style)
import Html.Events exposing (..)
import Http exposing (Error(..))
import List
import String exposing (fromInt)
import SvgResources exposing (svgHouse)
import Time
import Housing exposing (listHousingDecoder)



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
    { navbarState : Navbar.State, page : Page, housings : List Housing, error : Maybe String }


type Page
    = Booking
    | Profile
    | AddHousing


init : () -> ( Model, Cmd Msg )
init _ =
    let
        ( navbarState, navbarCmd ) =
            Navbar.initialState NavbarMsg
    in
    ( { navbarState = navbarState, page = Booking, housings = [], error = Nothing }, navbarCmd )



-- UPDATE


type Msg
    = NavbarMsg Navbar.State
    | NavTo Page
    | FetchHousings
    | ReceiveHousings (Result Error (List Housing))
    | Tick Time.Posix


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        NavbarMsg s ->
            ( { model | navbarState = s }, Cmd.none )

        NavTo p ->
            ( { model | page = p }, Cmd.none )

        FetchHousings ->
            Debug.todo "branch 'FetchHousings' not implemented"

        ReceiveHousings result ->
            case result of
                Ok housings ->
                    ( { model | housings = housings }, Cmd.none )

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
            ( model, fetchHousings )



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
                [ Navbar.itemLink [ href "#booking", onClick <| NavTo Booking ] [ text "Бронирование" ]
                , Navbar.itemLink [ href "#add-housing", onClick <| NavTo AddHousing ] [ text "Добавить жилье" ]
                , Navbar.itemLink [ href "#profile", onClick <| NavTo Profile ] [ text "Профиль" ]
                ]
            |> Navbar.view model.navbarState
        , Grid.container [ style "padding" "10" ]
            [ CDN.stylesheet
            , layoutChooser model
            ]
        ]


layoutChooser : Model -> Html Msg
layoutChooser model =
    case model.page of
        Booking ->
            div []
                [ Grid.row [ Row.centerLg ] <|
                    List.map housingCard model.housings,
                    text <| case model.error of
                        Just err ->
                            "Error: " ++ err

                        Nothing ->
                            ""
                ]

        Profile ->
            text "Profile page is not yet implemented"

        AddHousing ->
            text "AddHousing page is not yet implemented"


housingCard : Housing -> Column Msg
housingCard h =
    Grid.col [ Col.lgAuto ]
        [ Card.config [ Card.align Text.alignXsCenter ]
            |> Card.header [] [ svgHouse ]
            |> Card.block [ Block.align Text.alignLgLeft ]
                [ Block.titleH3 [] [ text <| show h.housingType ]
                , Block.text [] [ text <| fromInt h.price ++ " руб./ночь" ]
                , Block.custom <| Button.button [ Button.outlinePrimary ] [ text "Забронировать" ]
                ]
            |> Card.view
        ]



-- HTTP


fetchHousings : Cmd Msg
fetchHousings =
    Http.get
        { url = "http://localhost:8080/housing/all"
        , expect = Http.expectJson ReceiveHousings listHousingDecoder
        }


