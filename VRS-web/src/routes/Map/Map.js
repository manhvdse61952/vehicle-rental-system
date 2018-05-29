import React, { Component } from 'react';
import _ from 'lodash';
import { compose, withProps, lifecycle } from 'recompose';
import {
    GoogleMap,
    Marker,
    withGoogleMap,
    withScriptjs
} from 'react-google-maps';
import { SearchBox } from 'react-google-maps/lib/components/places/SearchBox';
import { Row, Container, Col } from 'reactstrap';

const MapWithASearchBox = compose(
    withProps({
        googleMapURL:
            'https://maps.googleapis.com/maps/api/js?key=AIzaSyC4R6AN7SmujjPUIGKdyao2Kqitzr1kiRg&v=3.exp&libraries=geometry,drawing,places',
        loadingElement: <div style={{ height: `100%` }} />,
        containerElement: <div style={{ height: `400px` }} />,
        mapElement: <div style={{ height: `100%` }} />
    }),
    lifecycle({
        componentWillMount() {
            const refs = {};
            this.setState({
                bounds: null,
                center: {
                    lat: 41.9,
                    lng: -87.624
                },
                markers: [],
                onMapMounted: ref => {
                    refs.map = ref;
                },
                onBoundsChanged: () => {
                    console.log('bounds change');
                    this.setState({
                        bounds: refs.map.getBounds()
                    });
                },
                onSearchBoxMounted: ref => {
                    refs.searchBox = ref;
                },
                onPlacesChanged: () => {
                    console.log('place change');
                    const places = refs.searchBox.getPlaces();
                    const bounds = new window.google.maps.LatLngBounds();
                    places.forEach(place => {
                        if (place.geometry.viewport) {
                            console.log('union');
                            bounds.union(place.geometry.viewport);
                        } else {
                            console.log('extend');
                            bounds.extend(place.geometry.location);
                        }
                    });
                    const nextMarkers = places.map(place => ({
                        position: place.geometry.location
                    }));
                    const nextCenter = _.get(
                        nextMarkers,
                        '0.position',
                        this.state.center
                    );
                    this.setState({
                        center: nextCenter,
                        markers: nextMarkers
                    });
                    // refs.map.fitBounds(bounds);
                }
            });
        }
    }),
    withScriptjs,
    withGoogleMap
)(props => (
    <GoogleMap
        ref={props.onMapMounted}
        defaultZoom={15}
        center={props.center}
        onBoundsChanged={props.onBoundsChanged}
    >
        <SearchBox
            ref={props.onSearchBoxMounted}
            bounds={props.bounds}
            controlPosition={window.google.maps.ControlPosition.TOP_LEFT}
            onPlacesChanged={props.onPlacesChanged}
        >
            <input
                type="text"
                placeholder="Customized your placeholder"
                style={{
                    boxSizing: `border-box`,
                    border: `1px solid transparent`,
                    width: `240px`,
                    height: `32px`,
                    marginTop: `27px`,
                    padding: `0 12px`,
                    borderRadius: `3px`,
                    boxShadow: `0 2px 6px rgba(0, 0, 0, 0.3)`,
                    fontSize: `14px`,
                    outline: `none`,
                    textOverflow: `ellipses`
                }}
            />
        </SearchBox>
        {props.markers.map((marker, index) => (
            <Marker key={index} position={marker.position} />
        ))}
    </GoogleMap>
));

class MyFancyComponent extends React.PureComponent {
    state = {
        isMarkerShown: false
    };

    componentDidMount() {
        this.delayedShowMarker();
    }

    delayedShowMarker = () => {
        setTimeout(() => {
            this.setState({ isMarkerShown: true });
        }, 3000);
    };

    handleMarkerClick = () => {
        this.setState({ isMarkerShown: false });
        this.delayedShowMarker();
    };

    render() {
        return (
            <MapWithASearchBox
                isMarkerShown={this.state.isMarkerShown}
                onMarkerClick={this.handleMarkerClick}
            />
        );
    }
}

class Map extends Component {
    render() {
        return <MyFancyComponent />;
    }
}

export default Map;
