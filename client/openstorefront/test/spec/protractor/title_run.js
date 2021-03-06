/* Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
*
* Licensed under the Apache License, Version 2.0 (the 'License');
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an 'AS IS' BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

'use strict';

// Will change for v1.3 to ClearingHouse?
var pageTitle = 'DI2E Clearinghouse';

describe('title__Page Title', function() {
  it('has a title of- ' + pageTitle, function() {
    browser.get(theSite, 2500);
    browser.driver.sleep(12000);
    expect(browser.getTitle()).toEqual(pageTitle);
  }, 25000);
});
