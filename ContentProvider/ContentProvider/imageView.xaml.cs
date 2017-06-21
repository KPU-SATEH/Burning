using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;

namespace ContentProvider
{
    /// <summary>
    /// Interaction logic for imageView.xaml
    /// </summary>
    public partial class imageView : Window
    {
        int num = 0;
        int maxNum = Data.Count();
        BitmapImage img;


        public imageView()
        {
            InitializeComponent();

            img = new BitmapImage(new Uri(Data.Read(num)));

            image.Source = img;
        }

        private void buttonLeft_Click(object sender, RoutedEventArgs e)
        {
            if (num == 0)
                return;
            else
            {
                num--;

                img = new BitmapImage(new Uri(Data.Read(num)));
                image.Source = img;
            }
        }

        private void buttonRight_Click(object sender, RoutedEventArgs e)
        {
            if (num == maxNum - 1)
                return;
            else
            {
                num++;

                img = new BitmapImage(new Uri(Data.Read(num)));
                image.Source = img;
            }
        }
    }
}
