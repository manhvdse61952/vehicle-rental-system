import { combineReducers } from 'redux';
import { reducer as form } from 'redux-form';
import loginReducer from '../routes/Login/reducers/loginReducer';
import { firebaseStateReducer as firebase } from 'react-redux-firebase';

const IndexReducer = combineReducers({
  form,
  firebase,
  loginReducer
});

export default IndexReducer;
