export default {
    items: [
        {
            name: 'Dashboard',
            url: '/dashboard',
            icon: 'icon-speedometer',
            badge: {
                variant: 'info',
                text: 'NEW'
            }
        },
        {
            title: true,
            name: 'Theme',
            wrapper: {
                // optional wrapper object
                element: '', // required valid HTML5 element tag
                attributes: {} // optional valid JS object with JS API naming ex: { className: "my-class", style: { fontFamily: "Verdana" }, id: "my-id"}
            },
            class: '' // optional class names space delimited list for title item ex: "text-center"
        },
        {
            name: 'Map',
            url: '/home/map',
            icon: 'icon-drop'
        },
        {
            name: 'Test',
            url: '/home/testrouter',
            icon: 'icon-pencil'
        },
        {
            name: 'Approve Employee',
            url: '/home/approve-employee',
            icon: 'icon-puzzle'
        },
        {
            name: 'Charts',
            url: '/charts',
            icon: 'icon-pie-chart'
        },
        {
            divider: true
        },
        {
            title: true,
            name: 'Extras'
        },
        {
            name: 'Pages',
            url: '/pages',
            icon: 'icon-star',
            children: [
                {
                    name: 'Login',
                    url: '/login',
                    icon: 'icon-star'
                },
                {
                    name: 'Register',
                    url: '/register',
                    icon: 'icon-star'
                },
                {
                    name: 'Error 404',
                    url: '/404',
                    icon: 'icon-star'
                },
                {
                    name: 'Error 500',
                    url: '/500',
                    icon: 'icon-star'
                }
            ]
        }
    ]
};
