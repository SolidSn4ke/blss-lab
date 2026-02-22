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
import List
import String exposing (fromInt)
import SvgResources exposing (svgHouse)



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
    { navbarState : Navbar.State, page : Page }


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
    ( { navbarState = navbarState, page = Booking }, navbarCmd )



-- UPDATE


type Msg
    = NavbarMsg Navbar.State
    | NavTo Page


update : Msg -> Model -> ( Model, Cmd Msg )
update msg model =
    case msg of
        NavbarMsg s ->
            ( { model | navbarState = s }, Cmd.none )

        NavTo p ->
            ( { model | page = p }, Cmd.none )



-- SUBSCRIPTIONS


subscriptions : Model -> Sub Msg
subscriptions model =
    Navbar.subscriptions model.navbarState NavbarMsg



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
            Grid.row [ Row.centerLg ] <|
                List.map housingCard hList

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
                [ Block.titleH3 [] [ text <| show h.hType ]
                , Block.text [] [ text <| fromInt h.price ++ " руб./ночь" ]
                , Block.custom <| Button.button [ Button.outlinePrimary ] [ text "Забронировать" ]
                ]
            |> Card.view
        ]


hList : List Housing
hList =
    [ { price = 12, hType = Room }, { price = 23, hType = Appartment }, { price = 315, hType = Hotel }, { price = 1000, hType = House }, { price = 2000, hType = Hotel } ]



-- HTTP
-- getRandomQuote : Cmd Msg
-- getRandomQuote =
--     Http.get
--         { url = "https://elm-lang.org/api/random-quotes"
--         , expect = Http.expectJson GotQuote quoteDecoder
--         }
-- quoteDecoder : Decoder Quote
-- quoteDecoder =
--     map4 Quote
--         (field "quote" string)
--         (field "source" string)
--         (field "author" string)
--         (field "year" int)
