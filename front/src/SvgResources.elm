module SvgResources exposing (..)

import Svg exposing (Svg, svg)
import Svg.Attributes exposing (d, fill, height, stroke, strokeLinecap, strokeLinejoin, strokeWidth, viewBox, width)


svgHouse : Svg a
svgHouse =
    svg
        [ width "100px"
        , height "100px"
        , viewBox "0 0 24 24"
        , fill "none"
        ]
        [ Svg.path
            [ d "M5 9.77746V16.2C5 17.8802 5 18.7203 5.32698 19.362C5.6146 19.9265 6.07354 20.3854 6.63803 20.673C7.27976 21 8.11984 21 9.8 21H14.2C15.8802 21 16.7202 21 17.362 20.673C17.9265 20.3854 18.3854 19.9265 18.673 19.362C19 18.7203 19 17.8802 19 16.2V5.00002M21 12L15.5668 5.96399C14.3311 4.59122 13.7133 3.90484 12.9856 3.65144C12.3466 3.42888 11.651 3.42893 11.0119 3.65159C10.2843 3.90509 9.66661 4.59157 8.43114 5.96452L3 12M14 21V15H10V21"
            , stroke "#000000"
            , strokeWidth "2"
            , strokeLinecap "round"
            , strokeLinejoin "round"
            ]
            []
        ]
