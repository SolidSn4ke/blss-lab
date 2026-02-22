module Housing exposing (..)
import Json.Decode exposing (Decoder, map3, field, int, string, fail, succeed, andThen, float, map6)


type alias Housing =
    { id : Int, price : Int, rating : Float, numOfBeds : Int, housingType : HousingType, address : Address }


type HousingType
    = Room
    | House
    | Hotel
    | Appartment
    | Hostel


type alias Address =
    { id : Int
    , street : String
    , country : String
    }


show : HousingType -> String
show t =
    case t of
        Room ->
            "Комната"

        House ->
            "Дом"

        Hotel ->
            "Отель"

        Appartment ->
            "Квартира"

        Hostel ->
            "Хостел"

housingDecoder : Decoder Housing
housingDecoder =
    map6 Housing
        (field "id" int)
        (field "price" int)
        (field "rating" float)
        (field "numOfBeds" int)
        (field "housingType" housingTypeDecoder)
        (field "address" addressDecoder)


housingTypeDecoder : Decoder HousingType
housingTypeDecoder =
    string |> andThen (\s -> case String.toLower s of
        "room" ->
            succeed Room

        "house" ->
            succeed House

        "hotel" ->
            succeed Hotel

        "appartment" ->
            succeed Appartment

        "hostel" ->
            succeed Hostel

        _ ->
            fail <| "Unknown housing type: " ++ s
    )


addressDecoder : Decoder Address
addressDecoder =
    map3 Address
        (field "id" int)
        (field "street" string)
        (field "country" string)


listHousingDecoder : Decoder (List Housing)
listHousingDecoder =
    Json.Decode.list housingDecoder