var ws = new ReconnectingWebSocket("ws://localhost:8080/domain/socket");
ws.onopen = function () {
    console.log("opened ws");
};
var eventHandlers = {};
function invokeHandlers(message) {
    $.each(eventHandlers[message.event], function (_, handler) {
        console.log(handler);
        handler(message);
    })
}
function subscribe(event, handler) {
    if(!$.isArray(eventHandlers[event])) eventHandlers[event] = [];
    console.log(handler);
    eventHandlers[event].push(handler);
}
ws.onmessage = function (message) {
    var parse = JSON.parse(message.data);
    if($.isArray(eventHandlers[parse.event])) {
        invokeHandlers(parse);
    }
};

var NewUserButton = React.createClass({
    handleClick: function() {
        $.ajax('http://localhost:8080/domain/accounts', {
            method: 'POST',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify({"name":"User", "email":"foo@bar.tld"})
        });
    },
    render: function() {
        return (
            <button onClick={this.handleClick}>Add User</button>
        );
    }
});

var NewProductButton = React.createClass({
    handleClick: function() {
        $.ajax('http://localhost:8080/domain/accounts', {
            method: 'POST',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify({"name":"Product", "email":"foo@bar.tld", "accountType":"BEEN"})
        });
    },
    render: function() {
        return (
            <button onClick={this.handleClick}>Add Product</button>
        );
    }
});

var ToUserPageButton = React.createClass({
    handleClick: function() {
        ReactDOM.unmountComponentAtNode(document.getElementById('content'));
        ReactDOM.render(<UserPage />, document.getElementById('content'));
    },
    render: function() {
        return (
            <button onClick={this.handleClick}>User List</button>
        );
    }
});

var UserWithBalance = React.createClass({
    subtractBalance: function(message) {
        if(message.id === this.props.user.id) {
            this.props.user.balance -= message.amount;
            this.setState({user: this.props.user});
        }
    },
    addBalance: function(message) {
        if(message.id === this.props.user.id) {
            this.props.user.balance += message.amount;
            this.setState({user: this.props.user});
        }
    },
    componentDidMount: function() {
        subscribe('AccountDebitedEventWebsocketMessage', this.subtractBalance);
        subscribe('AccountCreditedEventWebsocketMessage', this.addBalance);
    },
    render: function() {
        return (
            <div>
                <User user={this.props.user} />
                {this.props.user.balance}
            </div>
        );
    }
});

var currentUser;
var User = React.createClass({
    handleUserClick: function() {
        currentUser = this.props.user;
        ReactDOM.unmountComponentAtNode(document.getElementById('content'));
        ReactDOM.render(<ProductPage />, document.getElementById('content'));
    },
    handleProductClick: function() {
        $.ajax('http://localhost:8080/domain/accounts/transaction', {
            method: 'POST',
            contentType: 'application/json',
            dataType: 'json',
            data: JSON.stringify({"fromAccount":currentUser.id, "toAccount": this.props.user.id, "amount": 10})
        });
    },
    handleClick: function () {
        if(this.props.user.accountType === 'USER') {
            this.handleUserClick();
        }
        else {
            this.handleProductClick();
        }
    },
    render: function() {
        return (
            <div id="user_{this.props.user.id}" className="user" onClick={this.handleClick}>
                <div className="image"><img src="http://dummyimage.com/90" alt="{this.props.user.name}s avatar" /></div>
                <div className="name">{this.props.user.name}</div>
            </div>
        );
    }
});

var UserList = React.createClass({
    getInitialState: function() {
        return {data: []};
    },
    loadUserList: function() {
        if(this.isMounted()) {
            $.ajax({
                url: this.props.url,
                dataType: 'json',
                cache: false,
                success: function(data) {
                    this.setState({data: data});
                }.bind(this),
                error: function(xhr, status, err) {
                    console.error(this.props.url, status, err.toString());
                }.bind(this)
            });
        }
    },
    appendUser: function (user) {
        if(this.isMounted()) {
            this.state.data.push(user);
            this.setState({data: this.state.data});
        }
    },
    componentDidMount: function() {
        this.loadUserList();
        subscribe('AccountCreatedEventWebsocketMessage', this.appendUser);
    },
    render: function() {
        var userNodes = this.state.data.map(function (user) {
            return (
                <li key={user.id} className="user">
                    <User user={user} />
                </li>
            );
        });
        return (
            <div className="userList" ref="userList">
                <ul id="users">
                    {userNodes}
                </ul>
            </div>
        );
    }
});
var UserPage = React.createClass({
    render: function() {
        return (
            <div className="userPage">
                <NewUserButton />
                <UserList url="/domain/accounts/USER" />
            </div>
        );
    }
});
var ProductPage = React.createClass({
    render: function() {
        return (
            <div className="userPage">
                <NewProductButton />
                <UserWithBalance user={currentUser} />
                <ToUserPageButton />
                <UserList url="/domain/accounts/BEEN" />
            </div>
        );
    }
});
ReactDOM.render(<UserPage />, document.getElementById('content'));