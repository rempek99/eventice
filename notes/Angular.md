# **Angular**

## **Components**

Every component has a few main parts:

1. A [@Component](https://angular.dev/api/core/Component) [decorator](https://www.typescriptlang.org/docs/handbook/decorators.html) that contains some configuration used by Angular.
2. An HTML template that controls what renders into the DOM.
3. A [CSS selector](https://developer.mozilla.org/docs/Learn/CSS/Building_blocks/Selectors) that defines how the component is used in HTML.
4. A TypeScript class with behaviors, such as handling user input or making requests to a server.

Example component file:

```ts
// user-profile.ts
@Component({
  selector: "user-profile",
  templateUrl: "user-profile.html",
  styleUrl: "user-profile.css",
})
export class UserProfile {
  // Component behavior is defined in here
}
```

You can create a binding to show some dynamic text in a template by using double curly-braces:

```ts
@Component({
  selector: "user-profile",
  template: `<h1>Profile for {{ userName() }}</h1>`,
})
export class TodoListItem {
  userName = signal("pro_programmer_123");
}
```

```ts
@Component({
  /*...*/
  // Set the `disabled` property of the button based on the value of `isValidUserId`.
  template: `<button [disabled]="isValidUserId()">Save changes</button>`,
})
export class UserProfile {
  isValidUserId = signal(false);
}
```

```html
<!-- Bind the `role` attribute on the `<ul>` element to value of `listRole`. -->
<ul [attr.role]="listRole()"></ul>
```

```ts
@Component({
  /*...*/
  // Add an 'click' event handler that calls the `cancelSubscription` method.
  template: `<button (click)="cancelSubscription()">
    Cancel subscription
  </button>`,
})
export class UserProfile {
  /* ... */

  cancelSubscription() {
    /* Your event handling code goes here. */
  }
}
```

```html
<h1>User profile</h1>
@if (isAdmin()) {
<h2>Admin settings</h2>
<!-- ... -->
} @else {
<h2>User settings</h2>
<!-- ... -->
}
```

```html
<h1>User profile</h1>
<ul class="user-badge-list">
  @for (badge of badges(); track badge.id) {
  <li class="user-badge">{{badge.name}}</li>
  }
</ul>
```

## **Signals**

- Signals may be either _writable_ or _read-only_.
- **[Writable signals](https://angular.dev/guide/signals#writable-signals)**  
  Writable signals provide an API for updating their values directly. You create writable signals by calling the [signal](https://angular.dev/api/core/signal) function with the signal's initial value:

```ts
const count = signal(0); // Signals are getter functions - calling them reads their value.console.log('The count is: ' + count());
```

To change the value of a writable signal, either .set() it directly:

```ts
count.set(3);
```

or use the .update() operation to compute a new value from the previous one:

```ts
// Increment the count by 1.
count.update((value) => value + 1);
```

Writable signals have the type [WritableSignal](https://angular.dev/api/core/WritableSignal).

- **[Computed signals](https://angular.dev/guide/signals#computed-signals)**  
  **Computed signal** are read-only signals that derive their value from other signals. You define computed signals using the [computed](https://angular.dev/api/core/computed) function and specifying a derivation:

```ts
const count: WritableSignal<number> = signal(0);
const doubleCount: Signal<number> = computed(() => count() * 2);
```

The doubleCount signal depends on the count signal. Whenever count updates, Angular knows that doubleCount needs to update as well.

- **[Effects](https://angular.dev/guide/signals#effects)**  
  Signals are useful because they notify interested consumers when they change. An **effect** is an operation that runs whenever one or more signal values change. You can create an effect with the [effect](https://angular.dev/api/core/effect) function:

```ts
effect(() => {
  console.log(`The current count is: ${count()}`);
});
```

## **Services**

Services are reusable pieces of code that can be injected.  
Similar to defining a component, services are comprised of the following:

- A **TypeScript decorator** that declares the class as an Angular service via [@Injectable](https://angular.dev/api/core/Injectable) and allows you to define what part of the application can access the service via the providedIn property (which is typically 'root') to allow a service to be accessed anywhere within the application.
- A **TypeScript class** that defines the desired code that will be accessible when the service is injected  
  Here is an example of a Calculator service.

```ts
import { Injectable } from "@angular/core";
@Injectable({ providedIn: "root" })
export class Calculator {
  add(x: number, y: number) {
    return x + y;
  }
}
```

When you want to use a service in a component, you need to:

1. Import the service
2. Declare a class field where the service is injected. Assign the class field to the result of the call of the built-in function inject which creates the service  
   Here’s what it might look like in the Receipt component:

```ts
import { Component, inject } from "@angular/core";
import { Calculator } from "./calculator";
@Component({
  selector: "app-receipt",
  template: `<h1>The total is {{ totalCost }}</h1>`,
})
export class Receipt {
  private calculator = inject(Calculator);
  totalCost = this.calculator.add(50, 25);
}
```

## **Components Input**

When you use a component, you commonly want to pass some data to it. A component specifies the data that it accepts by declaring inputs:

```ts
import { Component, input } from "@angular/core";
@Component({
  /*...*/
})
export class CustomSlider {
  // Declare an input named 'value' with a default value of zero.
  value = input(0);
  // Create a computed expression that reads the value input
  label = computed(() => `The slider's value is ${this.value()}`);
}
```

You can explicitly declare a type for the input by specifying a generic parameter to the function.  
If an input without a default value is not set, its value is undefined:

```ts
@Component({
  /*...*/
})
export class CustomSlider {
  // Produces an InputSignal<number | undefined> because `value` may not be set.
  value = input<number>();
}

// Declare a required input named value. Returns an `InputSignal<number>`.
value = input.required<number>();
```

```ts
@Component({
  selector: "custom-slider",
  /*...*/
})
export class CustomSlider {
  label = input("", { transform: trimString });
}

function trimString(value: string | undefined): string {
  return value?.trim() ?? "";
}
```

Passing input value to the components:

```html
<custom-slider [label]="systemVolume" />
```

## **Event Listeners**

Angular supports defining event listeners on an element in your template by specifying the event name inside parentheses along with a statement that runs every time the event occurs.

Native Events ([All JS Events list here](https://www.w3schools.com/jsref/dom_obj_event.asp))  
When you want to add event listeners to an HTML element, you wrap the event with parentheses, (), which allows you to specify a listener statement.

```ts
@Component({
  template: `
    <input type="text" (keyup)="updateField($event)" />
  `,
  ...
})
export class AppComponent {
  updateField(event: KeyboardEvent): void {
    console.log(`The user pressed: ${event.key}`);
  }
}

```

If your event handler should replace the native browser behavior, you can use the event object's [preventDefault method](https://developer.mozilla.org/en-US/docs/Web/API/Event/preventDefault):

```ts
@Component({
  template: `
    <a href="#overlay" (click)="showOverlay($event)">
  `,
  ...
})
export class AppComponent{
  showOverlay(event: PointerEvent): void {
    event.preventDefault();
    console.log('Show overlay without updating the URL!');
  }
}

```

## **Routing**

Angular Router (@angular/router) is the official library for managing navigation in Angular applications and a core part of the framework. It is included by default in all projects created by Angular CLI.

Routing in Angular is comprised of ==three primary parts==:

1. ==Routes== define which component displays when a user visits a specific URL.
2. ==Outlets== are placeholders in your templates that dynamically load and render components based on the active route.
3. ==Links== provide a way for users to navigate between different routes in your application without triggering a full page reload.

Routes of the app are defined in the routes.ts file (default: _src/app/app.routes.ts_)

Understanding how and when components load in Angular routing is crucial for building responsive web applications. Angular offers two primary strategies to control component loading behavior:

1. ==Eagerly loaded==: Components that are loaded immediately
2. ==Lazily loaded:== Components loaded only when needed  
   Each approach offers distinct advantages for different scenarios.

### Example simple [Eagerly loaded components]:

```ts
import { Routes } from "@angular/router";
import { HomePage } from "./home-page/home-page.component";
import { AdminPage } from "./about-page/admin-page.component";
export const routes: Routes = [
  // HomePage and LoginPage are both directly referenced in this config,
  // so their code is eagerly included in the same JavaScript bundle as this file.

  {
    path: "",
    component: HomePage,
  },
  {
    path: "admin",
    component: AdminPage,
  },
];
```

Eagerly loading route components like this means that the browser has to download and parse all of the JavaScript for these components as part of your initial page load, but the components are available to Angular immediately.  
While including more JavaScript in your initial page load leads to slower initial load times, this can lead to more seamless transitions as the user navigates through an application.

### Example [Lazily loaded components]:

You can use the loadComponent property to lazily load the JavaScript for a route only at the point at which that route would become active.

```ts
import { Routes } from "@angular/router";
export const routes: Routes = [
  // The HomePage and LoginPage components are loaded lazily at the point at which
  // their corresponding routes become active.
  {
    path: "login",
    loadComponent: () =>
      import("./components/auth/login-page").then((m) => m.LoginPage),
  },
  {
    path: "",
    loadComponent: () =>
      import("./components/home/home-page").then((m) => m.HomePage),
  },
];
```

The loadComponent property accepts a loader function that returns a Promise that resolves to an Angular component. In most cases, this function uses the standard [JavaScript dynamic import API](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Operators/import). You can, however, use any arbitrary async loader function.  
Lazily loading routes can significantly improve the load speed of your Angular application by removing large portions of JavaScript from the initial bundle. These portions of your code compile into separate JavaScript "chunks" that the router requests only when the user visits the corresponding route.

### Redirects:

```ts
import { BlogComponent } from "./home/blog.component";
const routes: Routes = [
  {
    path: "articles",
    redirectTo: "/blog",
  },
  {
    path: "blog",
    component: BlogComponent,
  },
];
```

### **Titles:**

Titles may be provided directly like:

```ts
import { Routes } from "@angular/router";
import { HomeComponent } from "./home/home.component";
import { AboutComponent } from "./about/about.component";
import { ProductsComponent } from "./products/products.component";
const routes: Routes = [
  {
    path: "",
    component: HomeComponent,
    title: "Home Page",
  },
  {
    path: "about",
    component: AboutComponent,
    title: "About Us",
  },
];
```

If you want to make more fancy titles (eg. AppName - ${title}), do so using the ==TitleStrategy.==  
The custom TitleStrategy service must be implemented (extending TitleStrategy class):

```ts
import { Injectable } from "@angular/core";
import { Title } from "@angular/platform-browser";
import { TitleStrategy, RouterStateSnapshot } from "@angular/router";
@Injectable()
export class AppTitleStrategy extends TitleStrategy {
  private readonly title = inject(Title);
  updateTitle(snapshot: RouterStateSnapshot): void {
    // PageTitle is equal to the "Title" of a route if it's set
    // If its not set it will use the "title" given in index.html
    const pageTitle = this.buildTitle(snapshot) || this.title.getTitle();
    this.title.setTitle(`MyAwesomeApp - ${pageTitle}`);
  }
}
```

Then may be used in the application config:

```ts
import { provideRouter, TitleStrategy } from "@angular/router";
import { AppTitleStrategy } from "./app-title.strategy";
export const appConfig = {
  providers: [
    provideRouter(routes),
    { provide: TitleStrategy, useClass: AppTitleStrategy },
  ],
};
```
