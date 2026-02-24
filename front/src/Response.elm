module Response exposing (..)
import Json.Decode exposing (Decoder)
import Housing exposing (Housing)
import Json.Decode exposing (map3)
import Json.Decode exposing (field)
import Housing exposing (housingDecoder)
import Json.Decode exposing (int)
import Json.Decode exposing (string)

type alias Response a =
    { entity : a
    , code : Int,
    message : String
    }

responseDecoder : Decoder (Response Housing)
responseDecoder = map3 Response
    (field "entity" housingDecoder)
    (field "code" int)
    (field "message" string)